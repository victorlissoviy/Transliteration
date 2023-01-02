package transliteraion;

/**
 * Class for convert Ukrainian name and last name to program format.
 */
public final class Transliteration {

  private Transliteration(){}

  private static String convertSymbol(final String symbol) {

    return symbol;
  }

  private static String convertSymbol(final Character symbol) {
    return convertSymbol(symbol.toString());
  }

  /**
   * Convert name and last name to custom format.
   *
   * @param name     name
   * @param lastname last name
   * @return custom format string
   */
  public static String convert(final String name, final String lastname) {
    StringBuilder resultLine = new StringBuilder();

    resultLine.append(convertSymbol(name.charAt(0)).toUpperCase());
    resultLine.append(convertSymbol(lastname.charAt(0)).toUpperCase());

    int n = lastname.length();
    for(int i = 1; i < n; i++) {
      resultLine.append(convertSymbol(lastname.charAt(i)));
    }

    return resultLine.toString();
  }
}
