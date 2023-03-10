package transliteration;

import exceptions.ErrorReadFileException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
    int i;
    for (i = 1; i < n; i++) {

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

    if (i == n
        && (symbol.toCharArray()[0] < 64 || symbol.toCharArray()[0] > 90)
        && (symbol.toCharArray()[0] < 97 || symbol.toCharArray()[0] > 122)) {

      firstSymbol = true;
      result = "";
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
   * @param lastName last name
   * @return custom format string
   */
  public String convert(final String name, final String lastName) {
    return convert(name, lastName, null, 0);
  }

  /**
   * Convert name and last name to custom format. <br>
   * Index is number to choose format result string.
   *
   * @param name     name
   * @param lastname lastname
   * @param surname  surname
   * @param index    index format
   * @return custom format string
   */
  public String convert(final String name, final String lastname, String surname, int index) {
    readFile();

    String result;

    switch (index) {

      case 1: {
        result = getTransLitFirstSymbol(name) + getTransLitFirstSymbol(surname) + getTransLitLine(lastname);
        break;
      }

      case 2: {
        result = getTransLitLine(name) + getTransLitLine(lastname);
        break;
      }

      case 3: {
        result = getTransLitLine(name) + getTransLitLine(surname) + getTransLitLine(lastname);
        break;
      }

      default:
        result = getTransLitFirstSymbol(name) + getTransLitLine(lastname);
    }

    return result;
  }

  private String getTransLitFirstSymbol(final String line) {

    if (line == null) {
      return "";
    }

    firstSymbol = true;

    String result = null;

    char oneSymbol = line.toLowerCase().charAt(0);
    String convertedSymbol = convertSymbol(oneSymbol);

    if (convertedSymbol != null && convertedSymbol.length() > 0) {

      result = convertedSymbol.substring(0, 1);

    }

    firstSymbol = false;

    return result;
  }

  private String getTransLitLine(final String line) {

    if (line == null) {
      return "";
    }

    StringBuilder resultLine = new StringBuilder();

    firstSymbol = true;

    String lowerLine = line.toLowerCase();
    String preparedLine = lowerLine.replace(masLinks[0][0], masLinks[0][1]);

    resultLine.append(convertSymbol(preparedLine.charAt(0)));

    firstSymbol = false;

    int n = preparedLine.length();
    for (int i = 1; i < n; i++) {

      resultLine.append(convertSymbol(preparedLine.charAt(i)));

      if (firstSymbol) {

        i++;
        if (i < n) {
          resultLine.append(convertSymbol(preparedLine.charAt(i)));
        }

        firstSymbol = false;
      }
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
      try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
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
