package ldapCreater;

import exceptions.ReadFileException;
import exceptions.ThreadWorkError;
import exceptions.WriteFileException;
import transliteration.Transliteration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for read file and create LDAP files (.ldif).
 */
public class LDAPCreater {
  private final String file;
  private int id;
  private int index;
  private int sizeData;
  private final String ou;
  private final Transliteration[] trs;
  private final int countThreads = Runtime.getRuntime().availableProcessors();
  private String[] fileData;
  private final List<String> userNames = new ArrayList<>();
  private final StringBuffer userLdifData;

  private synchronized int getId() {
    return id++;
  }

  private synchronized int getIndex() {
    return index++;
  }

  public LDAPCreater(String file, int idStart, String ou) {
    this.file = file;
    this.id = idStart;
    this.ou = ou;

    trs = new Transliteration[countThreads];
    for (int i = 0; i < countThreads; i++) {
      trs[i] = new Transliteration();
    }

    userLdifData = new StringBuffer();
  }

  private void readFile() {
    String error = null;

    try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {

      List<String> lines = new ArrayList<>();

      String line = br.readLine();

      while (line != null) {
        lines.add(line);

        line = br.readLine();
      }

      fileData = lines.toArray(new String[0]);

    } catch (FileNotFoundException e) {
      error = String.format("File not found: %s\n%s", file, e.getMessage());
    } catch (IOException e) {
      error = String.format("File cannot be read: %s\n%s", file, e.getMessage());
    }

    if (error != null) {
      throw new ReadFileException(error);
    }
  }

  /**
   * Function for create data for user.ldif file.
   */
  public void createLDAPInfo() {
    readFile();

    index = 0;
    sizeData = fileData.length;

    Thread[] threads = new Thread[countThreads];

    for (int i = 0; i < countThreads; i++) {
      int finalI = i;

      threads[i] = new Thread(() -> work(finalI));
      threads[i].start();
    }

    for (Thread thread : threads) {
      String error = null;

      try {

        thread.join();

      } catch (InterruptedException e) {

        error = String.format("Error in thread: %s", e.getMessage());
      }

      if (error != null) {
        throw new ThreadWorkError(error);
      }
    }

    String userFileName = "user.ldif";
    String error = null;

    try (FileWriter file = new FileWriter(userFileName);
         BufferedWriter bw = new BufferedWriter(file)) {

      bw.write(userLdifData.toString());

    } catch (IOException e) {
      error = String.format(
              "Error write to file: %s\n%s",
              userFileName,
              e.getMessage()
      );
    }

    if (error != null) {
      throw new WriteFileException(error);
    }
  }

  private synchronized boolean checkAndAddData(String LDAPData) {

    boolean notExist = !userNames.contains(LDAPData);

    if (notExist) {
      userNames.add(LDAPData);
    }

    return notExist;
  }

  private void work(int currentThreadIndex) {
    while (true) {
      int i = getIndex();

      if (i >= sizeData) {
        break;
      }

      String line = fileData[i];

      String LDAPData = getOneLDAP(line, currentThreadIndex);

      userLdifData.append(LDAPData);
    }
  }

  /**
   * Function for get LDAP string for one user.
   *
   * @param line line about user
   * @param i    number thread
   * @return LDAP String
   */
  public String getOneLDAP(String line, int i) {
    int id = getId();

    String[] lines = line.split(" ");
    String transLit = null;

    int itr;
    for (itr = 0; itr < 4; itr++) {

      transLit = trs[i].convert(lines[1], lines[0], lines[2], itr);

      boolean added = checkAndAddData(transLit);

      if (added) {
        break;
      }
    }

    String result = String.format("dn: cn=%1$s,ou=%2$s,dc=kubd,dc=kub\n"
                    + "objectClass: top\n"
                    + "objectClass: account\n"
                    + "objectClass: posixAccount\n"
                    + "objectClass: shadowAccount\n"
                    + "cn: %3$s\n"
                    + "uid: %1$s\n"
                    + "uidNumber: %4$d\n"
                    + "gidNumber: %4$d\n"
                    + "homeDirectory: /home/%1$s\n"
                    + "userPassword:\n"
                    + "loginShell: /bin/bash\n"
                    + "gecos: %1$s\n"
                    + "shadowLastChange: -1\n"
                    + "shadowMax: -1\n"
                    + "shadowWarning: 0\n",
            transLit, ou, line, id);

    if (itr == 4) {
      writeToErrorFile(result);

      result = "";
    }

    return result;
  }

  private void writeToErrorFile(String line) {

    String errorFileName = "errors.txt";
    String error = null;

    try (FileWriter file = new FileWriter(errorFileName, true);
         BufferedWriter bw = new BufferedWriter(file)) {

      bw.write(line);

    } catch (IOException e) {
      error = String.format(
              "Error write to file: %s\n%s",
              errorFileName,
              e.getMessage()
      );
    }

    if (error != null) {
      throw new WriteFileException(error);
    }
  }
}
