package users;

/**
 * Class for save user data.
 */
public class User {
  private final String fullName;
  private final String transLit;
  private final String pass;

  public User(String fullName, String transLit, String pass) {
    this.fullName = fullName;
    this.transLit = transLit;
    this.pass = pass;
  }

  public String getFullName() {
    return fullName;
  }

  public String getTransLit() {
    return transLit;
  }

  public String getPass() {
    return pass;
  }

  @Override
  public boolean equals(Object obj) {

    if (!(obj instanceof User)) {
      return false;
    }

    User o = (User) obj;
    return transLit.equals(o.getTransLit());
  }
}
