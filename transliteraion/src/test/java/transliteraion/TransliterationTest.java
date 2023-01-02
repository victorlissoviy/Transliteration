package transliteraion;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransliterationTest {

  @Test
  void readFileTest(){
    Path p = Paths.get("src/main/resources/charChange.txt");

    assertEquals("charChange.txt", p.getFileName().toString());
  }

}