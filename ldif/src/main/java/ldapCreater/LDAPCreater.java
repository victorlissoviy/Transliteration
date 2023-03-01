package ldapCreater;

import exceptions.ReadFileException;
import exceptions.ThreadWorkError;
import exceptions.WriteFileException;
import transliteration.Transliteration;
import users.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    private final int countThreads = 1;//Runtime.getRuntime().availableProcessors();
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

        try (FileInputStream fr = new FileInputStream(file);
             InputStream is = new BufferedInputStream(fr);
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {

            List<String> lines = new ArrayList<>();

            String line = br.readLine();

            while (line != null) {

                line = line.replace("\t", " ");

                line = line.trim();

                while (line.contains("  ")) {
                    line = line.replace("  ", " ");
                }

                String[] sublines = line.split(" ");

                String firstSubLine = formatString(sublines[0]);

                StringBuilder lineBuilder = new StringBuilder(firstSubLine);
                for(int i = 1; i < sublines.length; i++) {

                    String subLine = formatString(sublines[i]);

                    lineBuilder.append(" ").append(subLine);
                }
                line = lineBuilder.toString();

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

        try (FileOutputStream fos = new FileOutputStream(fileName);
             OutputStream os = new BufferedOutputStream(fos);
             OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {

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

        try (FileOutputStream fos = new FileOutputStream(fileName);
             OutputStream os = new BufferedOutputStream(fos);
             OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {

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

        try (FileOutputStream fos = new FileOutputStream(userFileName);
             OutputStream os = new BufferedOutputStream(fos);
             OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {

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

    private synchronized boolean checkAndAddData(User User) {

        boolean notExist = !users.contains(User);

        if (notExist) {
            addUser(User);
        }

        return notExist;
    }

    private void addUser(User user) {

        int currentId = getId();

        String result = String.format(
                "dn: cn=%1$s,ou=%2$s,dc=kubd,dc=kub\n"
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
                user.getTransLit(),
                ou,
                user.getFullName(),
                currentId,
                user.getPass()
        );

        users.add(user);
        userLdifData.append(result);
    }

    private void work(int currentThreadIndex) {
        while (true) {
            int i = getIndex();

            if (i >= sizeData) {
                break;
            }

            String line = fileData[i];

            getOneLDAP(line, currentThreadIndex);
        }
    }

    /**
     * Function for get LDAP string for one user.
     *
     * @param line line about user
     * @param i    number thread
     */
    public void getOneLDAP(String line, int i) {

        String[] lines = line.split(" ");

        String lastName = lines[0];
        String name = lines[1];
        String surName = null;

        if (lines.length == 3) {
            surName = lines[2];
        }

        String transLitName = null;

        int itr;
        for (itr = 0; itr < 4; itr++) {

            transLitName = trs[i].convert(name, lastName, surName, itr);

            String pass = genPass();

            User user = new User(line, transLitName, pass);

            boolean added = checkAndAddData(user);

            if (added) {
                break;
            }
        }

        if (itr == 4) {
            writeToErrorFile(transLitName);
        }
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

            if (
                    (
                            (ra <= 9) || (ra >= 17 && ra <= 42) || (ra >= 49)
                    )
                            && (ra != 25)
                            && (ra != 28)
                            && (ra != 31)
                            && (ra != 60)
            ) {

                result.append((char) (ra + 48));

                count++;
            }

            ra = r.nextInt(75);
        }

        return result.toString();
    }

    private String formatString(final String line) {
        String result = line.substring(0, 1).toUpperCase();

        result += line.substring(1).toLowerCase();

        return result;
    }
}
