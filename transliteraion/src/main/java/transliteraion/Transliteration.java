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

  private Transliteration() {
  }

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
    readFile();

    StringBuilder resultLine = new StringBuilder();

    resultLine.append(convertSymbol(name.charAt(0)).toUpperCase());
    resultLine.append(convertSymbol(lastname.charAt(0)).toUpperCase());

    int n = lastname.length();
    for (int i = 1; i < n; i++) {
      resultLine.append(convertSymbol(lastname.charAt(i)));
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