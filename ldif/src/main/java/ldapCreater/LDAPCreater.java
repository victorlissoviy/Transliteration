package ldapCreater;

import exceptions.ReadFileException;
import exceptions.ThreadWorkError;
import exceptions.WriteFileException;
import transliteration.Transliteration;
import users.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
  private final List<User> users = new ArrayList<>();
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

        line = line.trim();

        while (line.contains("  ")) {
          line = line.replace("  ", " ");
        }

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

    writeUserFile();
    writeUserToGroupFile();
    writeExtFile();
  }

  private void writeExtFile() {
    String fileName = file.substring(0, file.lastIndexOf(".")) + "ext.imp";

    String error = null;

    try (FileWriter file = new FileWriter(fileName);
         BufferedWriter bw = new BufferedWriter(file)) {

      for (User user : users) {
        String fullName = user.getFullName();
        String transLitName = user.getTransLit();
        String pass = user.getPass();

        String line = String.format("%s %s %s\n", fullName, transLitName, pass);

        bw.write(line);
      }

    } catch (IOException e) {
      error = String.format(
              "Error write to file: %s\n%s",
              fileName,
              e.getMessage()
      );
    }

    if (error != null) {
      throw new WriteFileException(error);
    }
  }

  private void writeUserToGroupFile() {
    String fileName = "addUserToGroup.ldif";

    String error = null;

    try (FileWriter file = new FileWriter(fileName);
         BufferedWriter bw = new BufferedWriter(file)) {

      String firstLines = String.format(
              "dn: cn=debet,ou=%s,dc=kubd,dc=kub\n" +
              "changetype: modify\n" +
              "add: memberuid\n" +
              "memberuid: sermozg\n" +
              "memberuid: test\n",
              ou
      );

      bw.write(firstLines);

      for (User user : users) {
        String transLitName = user.getTransLit();

        String line = String.format("memberuid: %s\n", transLitName);

        bw.write(line);
      }

    } catch (IOException e) {
      error = String.format(
              "Error write to file: %s\n%s",
              fileName,
              e.getMessage()
      );
    }

    if (error != null) {
      throw new WriteFileException(error);
    }
  }

  private void writeUserFile() {
    String userFileName = "users.ldif";
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

  private synchronized boolean checkAndAddData(User LDAPData) {

    boolean notExist = !users.contains(LDAPData);

    if (notExist) {
      users.add(LDAPData);
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
    String transLitName = null;
    String pass = genPass();

    int itr;
    for (itr = 0; itr < 4; itr++) {

      transLitName = trs[i].convert(lines[1], lines[0], lines[2], itr);

      User user = new User(line, transLitName, pass);

      boolean added = checkAndAddData(user);

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
                    + "userPassword: %5$s\n"
                    + "loginShell: /bin/bash\n"
                    + "gecos: %1$s\n"
                    + "shadowLastChange: -1\n"
                    + "shadowMax: -1\n"
                    + "shadowWarning: 0\n\n",
            transLitName, ou, line, id, pass);

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

  private String genPass() {
    StringBuilder result = new StringBuilder();

    Random r = new Random();

    int ra = r.nextInt(75);
    int count = 0;

    while (count < 8) {

      if ((ra <= 9) || (ra >= 17 && ra <= 42) || (ra >= 49)) {

        result.append((char) (ra + 48));

        count++;
      }

      ra = r.nextInt(75);
    }

    return result.toString();
  }
}
