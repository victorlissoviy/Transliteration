package main;

import transliteration.Transliteration;

public class Main {
  private Main() {}

  public static void main(String[] args) {
    Transliteration tr = new Transliteration();
    System.out.println(tr.convert("Віктор", "Лісовий"));
  }
}
