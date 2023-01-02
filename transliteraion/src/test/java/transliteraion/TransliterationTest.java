package transliteraion;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class TransliterationTest {

  @Test
  void readFileTest() {
    Path p = Paths.get("src/main/resources/charChange.txt");

    assertEquals("charChange.txt", p.getFileName().toString());

    Transliteration.convert("Name", "LastName");

    String[] actual = Transliteration.getMasLinks()[0];
    assertEquals(2, actual.length);
    assertEquals("зг", actual[0]);
    assertEquals("zgh", actual[1]);
  }
}