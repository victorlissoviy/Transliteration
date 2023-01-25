package main;

import ldapCreater.LDAPCreater;

public class Main {
  private Main() {
  }

  public static void main(String[] args) {

    if (args.length != 3) {
      System.out.println("Please enter arguments (filename, idStart, ou)");
      return;
    }

    String filename = args[0];
    int idStart = Integer.parseInt(args[1]);
    String ou = args[2];

    LDAPCreater ldapCreater = new LDAPCreater(filename, idStart, ou);

    ldapCreater.createLDAPInfo();
  }
}
