package ldapCreater;

import exceptions.ReadFileException;
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
  private final String ou;
  private final Transliteration[] trs;

  private final int countThreads = Runtime.getRuntime().availableProcessors();

  private synchronized int getId() {
    return id++;
  }

  private String[] fileData;

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
      error = String.format("Файл не знайдено: %s", file);
    } catch (IOException e) {
      error = String.format("Файл не иожливо прочитати: %s", file);
    }

    if (error != null) {
      throw new ReadFileException(error);
    }
  }

  private void createLDAPInfo() {
    readFile();

  }

  /**
   * Function for get LDAP string for one user.
   *
   * @param line line about user
   * @param i number thread
   * @return LDAP String
   */
  public String getOneLDAP(String line, int i) {
    int id = getId();

    String[] lines = line.split(" ");
    String transLit = trs[i].convert(lines[1], lines[0]);

    String result = String.format(
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

    return result;
  }
}
