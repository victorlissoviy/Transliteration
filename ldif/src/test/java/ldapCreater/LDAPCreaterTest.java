package ldapCreater;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class LDAPCreaterTest {

  @Test
  void getOneLDAP() {
    LDAPCreater ldapCreater = new LDAPCreater(null, 1, "knu3");

    String regLine = "dn: cn=OBushurova,ou=knu3,dc=kubd,dc=kub\\n" +
            "objectClass: top\\n" +
            "objectClass: account\\n" +
            "objectClass: posixAccount\\n" +
            "objectClass: shadowAccount\\n" +
            "cn: Бушурова Олена Станіславівна\\n" +
            "uid: OBushurova\\n" +
            "uidNumber: 1\\n" +
            "gidNumber: 1\\n" +
            "homeDirectory: /home/OBushurova\\n" +
            "userPassword: [0-9a-zA-Z]{8}\\n" +
            "loginShell: /bin/bash\\n" +
            "gecos: OBushurova\\n" +
            "shadowLastChange: -1\\n" +
            "shadowMax: -1\\n" +
            "shadowWarning: 0\\n";

    Pattern patern = Pattern.compile(regLine);
    Matcher matcher = patern.matcher(ldapCreater.getOneLDAP("Бушурова Олена Станіславівна", 0));

    assertTrue(matcher.find());
  }
}