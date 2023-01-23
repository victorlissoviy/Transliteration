package ldapCreater;

import exceptions.ReadFileException;
import exceptions.ThreadWorkError;
import transliteration.Transliteration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

  private synchronized int getId() {
    return id++;
  }

  private String[] fileData;
  private final List<String> userNames = new ArrayList<>();
  private final StringBuffer userLdifData;

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

    try (FileReader fr = new FileReader(file);
         BufferedReader br = new BufferedReader(fr)) {

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

        error = String.format(
                "Error in thread: %s",
                e.getMessage()
        );
      }

      if (error != null) {
        throw new ThreadWorkError(error);
      }
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
    String transLit = trs[i].convert(lines[1], lines[0]);

    boolean added = checkAndAddData(transLit);

    if (!added) {
      transLit = trs[i].convert(lines[1], lines[0]);
    }

    return String.format(
            "dn: cn=%1$s,ou=%2$s,dc=kubd,dc=kub\n" +
            "objectClass: top\n" +
            "objectClass: account\n" +
            "objectClass: posixAccount\n" +
            "objectClass: shadowAccount\n" +
            "cn: %3$s\n" +
            "uid: %1$s\n" +
            "uidNumber: %4$d\n" +
            "gidNumber: %4$d\n" +
            "homeDirectory: /home/%1$s\n" +
            "userPassword:\n" +
            "loginShell: /bin/bash\n" +
            "gecos: %1$s\n" +
            "shadowLastChange: -1\n" +
            "shadowMax: -1\n" +
            "shadowWarning: 0\n",
            transLit,
            ou,
            line,
            id
    );
  }
}
