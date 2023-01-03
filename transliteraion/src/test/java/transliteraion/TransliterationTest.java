package transliteraion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransliterationTest {

  @Test
  void readFileTest() {
    Transliteration.convert("Name", "LastName");

    String[] actual = Transliteration.getMasLinks()[0];
    assertEquals(2, actual.length);
    assertEquals("зг", actual[0]);
    assertEquals("zgh", actual[1]);
  }

  @Test
  void convertTest() {
    assertEquals("AAlushta", Transliteration.convert("Андрій", "Алушта"));
    assertEquals("BBorshchahivka", Transliteration.convert("Б", "Борщагівка"));
    assertEquals("BBorysenko", Transliteration.convert("Б", "Борисенко"));
    assertEquals("VVinnytsia", Transliteration.convert("Володимир", "Вінниця"));
    assertEquals("BHadiach", Transliteration.convert("Богдан", "Гадяч"));
    assertEquals("GGalagan", Transliteration.convert("Ґ", "Ґалаґан"));
    assertEquals("GGorgany", Transliteration.convert("Ґ", "Ґорґани"));
    assertEquals("DDonetsk", Transliteration.convert("Дмитро", "Донецьк"));
    assertEquals("ORivne", Transliteration.convert("Олег", "Рівне"));
    assertEquals("YeYenakiieve", Transliteration.convert("Є", "Єнакієве"));
    assertEquals("ZhZhytomyr", Transliteration.convert("Жанна", "Житомир"));
    assertEquals("ZhZhezheliv", Transliteration.convert("Жежелів", "Жежелів"));
    assertEquals("ZZakarpattia", Transliteration.convert("З", "Закарпаття"));
    assertEquals("KKazymyrchuk", Transliteration.convert("К", "Казимирчук"));
    assertEquals("MMedvyn", Transliteration.convert("М", "Медвин"));
    assertEquals("MMykhailenko", Transliteration.convert("М", "Михайленко"));
    assertEquals("IIvankiv", Transliteration.convert("І", "Іванків"));
    assertEquals("YYizhakevych", Transliteration.convert("Y", "Їжакевич"));
    assertEquals("KKadyivka", Transliteration.convert("K", "Кадиївка"));
    assertEquals("MMarine", Transliteration.convert("M", "Мар'їне"));
    assertEquals("YYosypivka", Transliteration.convert("Y", "Йосипівка"));
    assertEquals("SStryi", Transliteration.convert("S", "Стрий"));
    assertEquals("OOleksii", Transliteration.convert("O", "Олексій"));
    assertEquals("KKyiv", Transliteration.convert("K", "Київ"));
    assertEquals("KKovalenko", Transliteration.convert("K", "Коваленко"));
    assertEquals("LLebedyn", Transliteration.convert("L", "Лебедин"));
    assertEquals("LLeonid", Transliteration.convert("L", "Леонід"));
    assertEquals("MMykolaiv", Transliteration.convert("M", "Миколаїв"));
    assertEquals("MMarynych", Transliteration.convert("M", "Маринич"));
    assertEquals("NNizhyn", Transliteration.convert("N", "Ніжин"));
    assertEquals("NNataliia", Transliteration.convert("N", "Наталія"));
    assertEquals("OOdesa", Transliteration.convert("O", "Одеса"));
    assertEquals("OOnyshchenko", Transliteration.convert("O", "Онищенко"));
    assertEquals("PPoltava", Transliteration.convert("P", "Полтава"));
    assertEquals("PPetro", Transliteration.convert("P", "Петро"));
    assertEquals("RReshetylivka", Transliteration.convert("R", "Решетилівка"));
    assertEquals("RRybchynskyi", Transliteration.convert("R", "Рибчинський"));
    assertEquals("SSumy", Transliteration.convert("S", "Суми"));
    assertEquals("SSolomiia", Transliteration.convert("S", "Соломія"));
    assertEquals("TTernopil", Transliteration.convert("T", "Тернопіль"));
    assertEquals("TTrots", Transliteration.convert("T", "Троць"));
    assertEquals("UUzhhorod", Transliteration.convert("U", "Ужгород"));
    assertEquals("UUliana", Transliteration.convert("U", "Уляна"));
    assertEquals("FFastiv", Transliteration.convert("F", "Фастів"));
    assertEquals("FFilipchuk", Transliteration.convert("F", "Філіпчук"));
    assertEquals("KKharkiv", Transliteration.convert("K", "Харків"));
    assertEquals("KKhrystyna", Transliteration.convert("K", "Христина"));
    assertEquals("SStetsenko", Transliteration.convert("S", "Стеценко"));
    assertEquals("CChernivtsi", Transliteration.convert("C", "Чернівці"));
    assertEquals("SShevchenko", Transliteration.convert("S", "Шевченко"));
    assertEquals("SShostka", Transliteration.convert("S", "Шостка"));
    assertEquals("KKyshenky", Transliteration.convert("K", "Кишеньки"));
    assertEquals("SShcherbukhy", Transliteration.convert("S", "Щербухи"));
    assertEquals("HHoshcha", Transliteration.convert("H", "Гоща"));
    assertEquals("HHarashchenko", Transliteration.convert("H", "Гаращенко"));
    assertEquals("YYurii", Transliteration.convert("Y", "Юрій"));
    assertEquals("KKoriukivka", Transliteration.convert("K", "Корюківка"));
    assertEquals("YYahotyn", Transliteration.convert("Y", "Яготин"));
    assertEquals("YYaroshenko", Transliteration.convert("Y", "Ярошенко"));
    assertEquals("KKostiantyn", Transliteration.convert("K", "Костянтин"));
    assertEquals("ZZnamianka", Transliteration.convert("Z", "Знам'янка"));
    assertEquals("FFeodosiia", Transliteration.convert("F", "Феодосія"));
    assertEquals("RRozghon", Transliteration.convert("R", "Розгон"));
    assertEquals("ZZghorany", Transliteration.convert("Z", "Згорани"));
  }
}