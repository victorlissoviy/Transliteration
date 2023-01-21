package transliteration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransliterationTest {
  final Transliteration tr = new Transliteration();

  @Test
  void readFileTest() {
    tr.convert("Name", "LastName");

    String[] actual = Transliteration.getMasLinks()[0];
    assertEquals(2, actual.length);
    assertEquals("зг", actual[0]);
    assertEquals("zgh", actual[1]);
  }

  @Test
  void convertTest() {
    assertEquals("AAlushta", tr.convert("Андрій", "Алушта"));
    assertEquals("BBorshchahivka", tr.convert("Б", "Борщагівка"));
    assertEquals("BBorysenko", tr.convert("Б", "Борисенко"));
    assertEquals("VVinnytsia", tr.convert("Володимир", "Вінниця"));
    assertEquals("BHadiach", tr.convert("Богдан", "Гадяч"));
    assertEquals("GGalagan", tr.convert("Ґ", "Ґалаґан"));
    assertEquals("GGorgany", tr.convert("Ґ", "Ґорґани"));
    assertEquals("DDonetsk", tr.convert("Дмитро", "Донецьк"));
    assertEquals("ORivne", tr.convert("Олег", "Рівне"));
    assertEquals("YYenakiieve", tr.convert("Є", "Єнакієве"));
    assertEquals("ZZhytomyr", tr.convert("Жанна", "Житомир"));
    assertEquals("ZZhezheliv", tr.convert("Жежелів", "Жежелів"));
    assertEquals("ZZakarpattia", tr.convert("З", "Закарпаття"));
    assertEquals("KKazymyrchuk", tr.convert("К", "Казимирчук"));
    assertEquals("MMedvyn", tr.convert("М", "Медвин"));
    assertEquals("MMykhailenko", tr.convert("М", "Михайленко"));
    assertEquals("IIvankiv", tr.convert("І", "Іванків"));
    assertEquals("YYizhakevych", tr.convert("Y", "Їжакевич"));
    assertEquals("KKadyivka", tr.convert("K", "Кадиївка"));
    assertEquals("MMarine", tr.convert("M", "Мар'їне"));
    assertEquals("YYosypivka", tr.convert("Y", "Йосипівка"));
    assertEquals("SStryi", tr.convert("S", "Стрий"));
    assertEquals("OOleksii", tr.convert("O", "Олексій"));
    assertEquals("KKyiv", tr.convert("K", "Київ"));
    assertEquals("KKovalenko", tr.convert("K", "Коваленко"));
    assertEquals("LLebedyn", tr.convert("L", "Лебедин"));
    assertEquals("LLeonid", tr.convert("L", "Леонід"));
    assertEquals("MMykolaiv", tr.convert("M", "Миколаїв"));
    assertEquals("MMarynych", tr.convert("M", "Маринич"));
    assertEquals("NNizhyn", tr.convert("N", "Ніжин"));
    assertEquals("NNataliia", tr.convert("N", "Наталія"));
    assertEquals("OOdesa", tr.convert("O", "Одеса"));
    assertEquals("OOnyshchenko", tr.convert("O", "Онищенко"));
    assertEquals("PPoltava", tr.convert("P", "Полтава"));
    assertEquals("PPetro", tr.convert("P", "Петро"));
    assertEquals("RReshetylivka", tr.convert("R", "Решетилівка"));
    assertEquals("RRybchynskyi", tr.convert("R", "Рибчинський"));
    assertEquals("SSumy", tr.convert("S", "Суми"));
    assertEquals("SSolomiia", tr.convert("S", "Соломія"));
    assertEquals("TTernopil", tr.convert("T", "Тернопіль"));
    assertEquals("TTrots", tr.convert("T", "Троць"));
    assertEquals("UUzhhorod", tr.convert("U", "Ужгород"));
    assertEquals("UUliana", tr.convert("U", "Уляна"));
    assertEquals("FFastiv", tr.convert("F", "Фастів"));
    assertEquals("FFilipchuk", tr.convert("F", "Філіпчук"));
    assertEquals("KKharkiv", tr.convert("K", "Харків"));
    assertEquals("KKhrystyna", tr.convert("K", "Христина"));
    assertEquals("SStetsenko", tr.convert("S", "Стеценко"));
    assertEquals("CChernivtsi", tr.convert("C", "Чернівці"));
    assertEquals("SShevchenko", tr.convert("S", "Шевченко"));
    assertEquals("SShostka", tr.convert("S", "Шостка"));
    assertEquals("KKyshenky", tr.convert("K", "Кишеньки"));
    assertEquals("SShcherbukhy", tr.convert("S", "Щербухи"));
    assertEquals("HHoshcha", tr.convert("H", "Гоща"));
    assertEquals("HHarashchenko", tr.convert("H", "Гаращенко"));
    assertEquals("YYurii", tr.convert("Y", "Юрій"));
    assertEquals("KKoriukivka", tr.convert("K", "Корюківка"));
    assertEquals("YYahotyn", tr.convert("Y", "Яготин"));
    assertEquals("YYaroshenko", tr.convert("Y", "Ярошенко"));
    assertEquals("KKostiantyn", tr.convert("K", "Костянтин"));
    assertEquals("ZZnamianka", tr.convert("Z", "Знам'янка"));
    assertEquals("FFeodosiia", tr.convert("F", "Феодосія"));
    assertEquals("RRozghon", tr.convert("R", "Розгон"));
    assertEquals("ZZghorany", tr.convert("Z", "Згорани"));
  }
}