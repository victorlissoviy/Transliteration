package transliteration;

import exceptions.ErrorReadFileException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for convert Ukrainian name and last name to program format.
 */
public final class Transliteration {
  public static String[][] getMasLinks() {
    return masLinks;
  }

  private static String[][] masLinks;
  private boolean firstSymbol = true;

  public Transliteration() {
  }

  private String convertSymbol(final String symbol) {

    String lowSymbol = symbol.toLowerCase();

    String result = symbol;

    if (firstSymbol) {
      result = symbol.substring(0, 1).toUpperCase() + lowSymbol.substring(1);
    }

    int n = masLinks.length;
    for (int i = 1; i < n; i++) {

      String[] link = masLinks[i];

      if (link[0].contains(lowSymbol)) {

        if (link.length == 3) {
          if (firstSymbol) {
            result = link[2];
          } else {
            result = link[1];
          }
        } else {
          result = "";
        }

        break;
      }

    }

    return result;
  }

  private String convertSymbol(final Character symbol) {
    return convertSymbol(symbol.toString());
  }

  /**
   * Convert name and last name to custom format.
   *
   * @param name     name
   * @param lastname last name
   * @return custom format string
   */
  public String convert(final String name, final String lastname) {
    readFile();

    String lowerLastName = lastname.toLowerCase();
    String prepareLastName = lowerLastName.replace(masLinks[0][0], masLinks[0][1]);

    StringBuilder resultLine = new StringBuilder();

    firstSymbol = true;

    resultLine.append(convertSymbol(name.charAt(0)).charAt(0));
    resultLine.append(convertSymbol(prepareLastName.charAt(0)));

    firstSymbol = false;

    int n = prepareLastName.length();
    for (int i = 1; i < n; i++) {
      resultLine.append(convertSymbol(prepareLastName.charAt(i)));
    }

    return resultLine.toString();
  }

  private synchronized static void readFile() {
    if (masLinks != null) {
      return;
    }

    List<String> list = new ArrayList<>();

    try (InputStream is = Transliteration.class.getResourceAsStream("/links/charChange.txt")) {

      assert is != null;
      try (InputStreamReader isr = new InputStreamReader(is);
           BufferedReader br = new BufferedReader(isr)) {

        String line = br.readLine();

        while (line != null) {
          list.add(line);

          line = br.readLine();
        }

      }
    } catch (IOException e) {
      throw new ErrorReadFileException(e);
    }

    int n = list.size();

    masLinks = new String[n][];

    for (int i = 0; i < n; i++) {
      masLinks[i] = list.get(i).split("\t");
    }
  }
}
