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
   *             first - name<br>
   *             second - last name
   */
  public static void main(final String[] args) {

    if (args.length < 2) {
      System.err.println("Please enter two arguments");
      return;
    }

    String name = args[0];
    String lastname = args[1];

    String result = Transliteration.convert(name, lastname);

    System.out.println(result);
  }
}
