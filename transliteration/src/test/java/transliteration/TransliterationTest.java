package transliteration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
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
    assertEquals("YYizhakevych", tr.convert("Ї", "Їжакевич"));
    assertEquals("KKadyivka", tr.convert("К", "Кадиївка"));
    assertEquals("MMarine", tr.convert("М", "Мар'їне"));
    assertEquals("YYosypivka", tr.convert("Й", "Йосипівка"));
    assertEquals("SStryi", tr.convert("С", "Стрий"));
    assertEquals("OOleksii", tr.convert("О", "Олексій"));
    assertEquals("KKyiv", tr.convert("К", "Київ"));
    assertEquals("KKovalenko", tr.convert("К", "Коваленко"));
    assertEquals("LLebedyn", tr.convert("Л", "Лебедин"));
    assertEquals("LLeonid", tr.convert("Л", "Леонід"));
    assertEquals("MMykolaiv", tr.convert("М", "Миколаїв"));
    assertEquals("MMarynych", tr.convert("М", "Маринич"));
    assertEquals("NNizhyn", tr.convert("Н", "Ніжин"));
    assertEquals("NNataliia", tr.convert("Н", "Наталія"));
    assertEquals("OOdesa", tr.convert("О", "Одеса"));
    assertEquals("OOnyshchenko", tr.convert("О", "Онищенко"));
    assertEquals("PPoltava", tr.convert("П", "Полтава"));
    assertEquals("PPetro", tr.convert("П", "Петро"));
    assertEquals("RReshetylivka", tr.convert("Р", "Решетилівка"));
    assertEquals("RRybchynskyi", tr.convert("Р", "Рибчинський"));
    assertEquals("SSumy", tr.convert("С", "Суми"));
    assertEquals("SSolomiia", tr.convert("С", "Соломія"));
    assertEquals("TTernopil", tr.convert("Т", "Тернопіль"));
    assertEquals("TTrots", tr.convert("Т", "Троць"));
    assertEquals("UUzhhorod", tr.convert("У", "Ужгород"));
    assertEquals("UUliana", tr.convert("У", "Уляна"));
    assertEquals("FFastiv", tr.convert("Ф", "Фастів"));
    assertEquals("FFilipchuk", tr.convert("Ф", "Філіпчук"));
    assertEquals("KKharkiv", tr.convert("Х", "Харків"));
    assertEquals("KKhrystyna", tr.convert("Х", "Христина"));
    assertEquals("SStetsenko", tr.convert("С", "Стеценко"));
    assertEquals("CChernivtsi", tr.convert("Ч", "Чернівці"));
    assertEquals("SShevchenko", tr.convert("Ш", "Шевченко"));
    assertEquals("SShostka", tr.convert("Ш", "Шостка"));
    assertEquals("KKyshenky", tr.convert("К", "Кишеньки"));
    assertEquals("SShcherbukhy", tr.convert("Щ", "Щербухи"));
    assertEquals("HHoshcha", tr.convert("Г", "Гоща"));
    assertEquals("HHarashchenko", tr.convert("Г", "Гаращенко"));
    assertEquals("YYurii", tr.convert("Ю", "Юрій"));
    assertEquals("KKoriukivka", tr.convert("К", "Корюківка"));
    assertEquals("YYahotyn", tr.convert("Я", "Яготин"));
    assertEquals("YYaroshenko", tr.convert("Я", "Ярошенко"));
    assertEquals("KKostiantyn", tr.convert("К", "Костянтин"));
    assertEquals("ZZnamianka", tr.convert("З", "Знам'янка"));
    assertEquals("FFeodosiia", tr.convert("Ф", "Феодосія"));
    assertEquals("RRozghon", tr.convert("Р", "Розгон"));
    assertEquals("ZZghorany", tr.convert("З", "Згорани"));
  }

  @Test
  void manyConvertsTest() {
    String name = "Віктор";
    String lastname = "Лісовий";
    String surname = "Юрійович";

    assertEquals("VLisovyi", tr.convert(name, lastname, surname, 0));
    assertEquals("VYLisovyi", tr.convert(name, lastname, surname, 1));
    assertEquals("ViktorLisovyi", tr.convert(name, lastname, surname, 2));
    assertEquals("ViktorYuriiovychLisovyi", tr.convert(name, lastname, surname, 3));
  }
}