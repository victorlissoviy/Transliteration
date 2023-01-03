package transliteraion;

import exceptions.ConfigNotFoundException;
import exceptions.ErrorReadFileException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
  private static boolean firstSymbol = true;

  private Transliteration() {
  }

  private static String convertSymbol(final String symbol) {

    String lowSymbol = symbol.toLowerCase();

    String result = symbol;

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
    readFile();

    String prepareLastName = lastname.replace(masLinks[0][0], masLinks[0][1]);

    StringBuilder resultLine = new StringBuilder();

    firstSymbol = true;

    resultLine.append(convertSymbol(name.charAt(0)));
    resultLine.append(convertSymbol(prepareLastName.charAt(0)));

    firstSymbol = false;

    int n = lastname.length();
    for (int i = 1; i < n; i++) {
      resultLine.append(convertSymbol(prepareLastName.charAt(i)));
    }

    return resultLine.toString();
  }

  private static void readFile() {
    File file = new File("src/main/resources/charChange.txt");
    if (!file.exists() || !file.isFile()) {
      throw new ConfigNotFoundException("File not found");
    }

    List<String> list = new ArrayList<>();

    try (FileReader fr = new FileReader(file);
         BufferedReader br = new BufferedReader(fr)) {

      String line = br.readLine();

      while (line != null) {
        list.add(line);

        line = br.readLine();
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
