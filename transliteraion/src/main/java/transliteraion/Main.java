package transliteraion;

/**
 * Main class program.
 */
public final class Main {

  /**
   * Empty constructor.
   */
  private Main() {
  }

  /**
   * Main method in program.
   *
   * @param args input args:<br>
   *             first - last name<br>
   *             second - name
   */
  public static void main(final String[] args) {

    if (args.length < 2) {
      System.err.println("Please enter two arguments");
      return;
    }

    String name = args[1];
    String lastname = args[0];

    String result = Transliteration.convert(name, lastname);

    System.out.println(result);
  }
}
