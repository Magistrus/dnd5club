package club.dnd5.portal.controller.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import club.dnd5.portal.dto.item.ItemMagicDto;
import club.dnd5.portal.model.Alignment;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.items.MagicThingTable;
import club.dnd5.portal.model.items.Rarity;
import club.dnd5.portal.model.items.Weapon;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.datatable.ItemMagicTableRepository;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;
import club.dnd5.portal.repository.datatable.WeaponDatatableRepository;

@Controller
public class TraderToolController {
	public static final Random rnd = new Random();

	@Autowired
	private SpellDatatableRepository spellRepo;
	@Autowired
	private WeaponDatatableRepository weaponRepo;
	@Autowired
	private ItemMagicTableRepository mtRepo;

	@GetMapping("/tools/trader")
	public String getTreasuryTool(Model model) {
		model.addAttribute("metaTitle", "Генератор лавки торговца");
		model.addAttribute("metaUrl", "https://dnd5.club/tools/trader");
		model.addAttribute("metaDescription", "Генерация содержимого лавки торговца");
		return "tools/traders";
	}

	@GetMapping("/tools/trader/result")
	public String generateTraider(Model model, Integer result, Integer mageRang) {
		int coef = 0;
		if (mageRang == 1) {
			coef = -10;
		} else if (mageRang == 3) {
			coef = 10;
		}
		List<ItemMagicDto> list = new ArrayList<>();
		list.addAll(getMagicThings(result + coef, 1, 5, "А", 6));
		list.addAll(getMagicThings(result + coef, 6, 10, "Б", 4));
		list.addAll(getMagicThings(result + coef, 11, 15, "В", 4));
		list.addAll(getMagicThings(result + coef, 16, 20, "Г", 4));
		list.addAll(getMagicThings(result + coef, 21, 25, "Д", 4));
		list.addAll(getMagicThings(result + coef, 26, 30, "Е", 4));
		list.addAll(getMagicThings(result + coef, 31, 35, "Е1", 4));
		list.addAll(getMagicThings(result + coef, 36, 40, "Ж", 4));
		list.addAll(getMagicThings(result + coef, 41, 1000, "З", 4));
		Collections.sort(list, Comparator.comparing(t -> Integer.valueOf(t.getCostDMG())));
		model.addAttribute("items", list);
		return "fragments/trader :: view";
	}

	private List<ItemMagicDto> getMagicThings(Integer result, int start, int end, String tableName, int count) {
		if (result == null) {
			result = 1;
		}
		List<ItemMagicDto> list = new ArrayList<>();
		if (result >= start) {
			for (int i = 0; i < 1 + rnd.nextInt(count); i++) {
				int ri = 1 + rnd.nextInt(100);
				// System.out.println("table= " + tableName + " in " + ri);
				MagicThingTable mt = mtRepo.findOne(ri, tableName);
				if (mt != null) {
					ItemMagicDto dto = new ItemMagicDto(mt.getMagicThing());
					dto.setCost(getCost(mt.getMagicThing().getRarity()));
					if (tableName.equals("Б")) {
						if (ri == 91) {
							int zap = 4 + rnd.nextInt(4) + rnd.nextInt(4) + rnd.nextInt(4) + rnd.nextInt(4);
							dto.setName(dto.getName() + "(дополнительных заплаток " + zap + ")");
						}
					}
					if (tableName.equals("В")) {
						if (ri >= 82 && ri <= 84) {
							String effect = "";
							int er = 1 + rnd.nextInt(100);
							if (er <= 15) {
								effect = "веер";
							} else if (er <= 40) {
								effect = "дерево";
							} else if (er <= 50) {
								effect = "кнут";
							} else if (er <= 65) {
								effect = "лодка-лебедь";
							} else if (er <= 80) {
								effect = "птица";
							} else {
								effect = "якорь";
							}
							dto.setName(dto.getName() + " (Эффект: " + effect + ")");
						} else if (ri >= 85 && ri <= 87) {
							String effect = "";
							int er = 1 + rnd.nextInt(100);
							if (er <= 10) {
								effect = "Аберрации";
							} else if (er <= 20) {
								effect = "Звери";
							} else if (er <= 45) {
								effect = "Исчадия";
							} else if (er <= 55) {
								effect = "Небожителиь";
							} else if (er <= 75) {
								effect = "Нежить";
							} else if (er <= 80) {
								effect = "Растения";
							} else if (er <= 90) {
								effect = "Феи";
							} else if (er <= 100) {
								effect = "Элементали";
							} else {
								effect = "якорь";
							}
							dto.setName(dto.getName() + " (Вид существ: " + effect + ")");
						} else if (ri >= 88 && ri <= 89) {
							dto.setName(dto.getName() + " (Бобов: "
									+ (3 + rnd.nextInt(4) + rnd.nextInt(4) + rnd.nextInt(4)) + ")");
						}
					}
					if (tableName.equals("E")) {
						switch (ri) {
						case 66:
							dto.setName("Адамантиновый Доспех (кольчуга)");
							dto.setCost(Integer.valueOf(dto.getCostDMG()) + 75);
							break;
						case 67:
							dto.setName("Адамантиновый Доспех (кольчужная рубаха)");
							dto.setCost(Integer.valueOf(dto.getCostDMG()) + 50);
							break;
						case 68:
							dto.setName("Адамантиновый Доспех (чещуйчатый доспех)");
							dto.setCost(Integer.valueOf(dto.getCostDMG()) + 50);
							break;
						}
					}
					if (tableName.equals("E1")) {
						switch (ri) {
						case 15:
							dto.setName("Адамантиновый Доспех (кираса)");
							dto.setCost(Integer.valueOf(dto.getCostDMG()) + 400);
							break;
						case 16:
							dto.setName("Адамантиновый Доспех (наборной доспех)");
							dto.setCost(Integer.valueOf(dto.getCostDMG()) + 200);
							break;
						case 35:
							dto.setName("Доспех +1 (кожаный)");
							dto.setRarity(Rarity.UNCOMMON.getCyrilicName());
							dto.setCost(getCost(Rarity.UNCOMMON) + 10);
							break;
						case 36:
							dto.setName("Доспех +1 (кольчуга)");
							dto.setRarity(Rarity.UNCOMMON.getCyrilicName());
							dto.setCost(getCost(Rarity.UNCOMMON) + 75);
							break;
						case 37:
							dto.setName("Доспех +1 (кольчужная рубаха)");
							dto.setRarity(Rarity.UNCOMMON.getCyrilicName());
							dto.setCost(getCost(Rarity.UNCOMMON) + 50);
							break;
						case 38:
							dto.setName("Доспех +1 (чешуйчатый)");
							dto.setRarity(Rarity.UNCOMMON.getCyrilicName());
							dto.setCost(getCost(Rarity.UNCOMMON) + 50);
							break;
						case 60:
							dto.setName(dto.getName() + " " + getResistenceType());
							break;
						}
					}
					if (tableName.equals("Ж")) {
						switch (ri) {
						case 55:
							dto.setName("Адамантиновый Доспех (латы)");
							dto.setCost(Integer.valueOf(dto.getCostDMG()) + 1500);
							break;
						case 56:
							dto.setName("Адамантиновый Доспех (полулаты)");
							dto.setCost(Integer.valueOf(dto.getCostDMG()) + 750);
							break;
						case 65:
							dto.setName("Доспех +1 (кираса)");
							dto.setRarity(Rarity.UNCOMMON.getCyrilicName());
							dto.setCost(getCost(Rarity.UNCOMMON) + 400);
							break;
						case 66:
							dto.setName("Доспех +1 (кираса)");
							dto.setRarity(Rarity.UNCOMMON.getCyrilicName());
							dto.setCost(getCost(Rarity.UNCOMMON) + 400);
							break;
						case 67:
							dto.setName("Доспех +1 (проклёпанная кожа)");
							dto.setRarity(Rarity.UNCOMMON.getCyrilicName());
							dto.setCost(getCost(Rarity.UNCOMMON) + 400);
							break;
						case 68:
							dto.setName("Доспех +2 (кожаный)");
							dto.setRarity(Rarity.RARE.getCyrilicName());
							dto.setCost(getCost(Rarity.RARE) + 10);
							break;
						case 69:
							dto.setName("Доспех +2 (кольчуга)");
							dto.setRarity(Rarity.RARE.getCyrilicName());
							dto.setCost(getCost(Rarity.RARE) + 75);
							break;
						case 70:
							dto.setName("Доспех +2 (кольчужная рубаха)");
							dto.setRarity(Rarity.RARE.getCyrilicName());
							dto.setCost(getCost(Rarity.RARE) + 50);
							break;
						case 71:
							dto.setName("Доспех +2 (чешуйчатый)");
							dto.setRarity(Rarity.RARE.getCyrilicName());
							dto.setCost(getCost(Rarity.RARE) + 50);
							break;
						case 89:
							String aligment = Alignment.values()[rnd.nextInt(Alignment.values().length)]
									.getCyrilicName();
							dto.setName(dto.getName() + "(Мировоззрение: " + aligment + ")");
							break;
						case 91:
							int rg = 1 + rnd.nextInt(20);
							String golemType = "";
							if (rg >= 1 && rg <= 5)
								golemType = "Глинянный";
							else if (rg == 6)
								golemType = "Железный";
							else if (rg >= 7 && rg <= 8)
								golemType = "Каменный";
							else if (rg >= 9 && rg <= 20)
								golemType = "Мясной (из плоти)";
							dto.setName(dto.getName() + "(" + golemType + ")");
							break;
						}
					}
					if (tableName.equals("З")) {
						switch (ri) {
						case 42:
						case 43:
							dto.setName("Доспех +1 (латы)");
							dto.setRarity(Rarity.UNCOMMON.getCyrilicName());
							dto.setCost(getCost(Rarity.UNCOMMON) + 1500);
							break;
						case 44:
						case 45:
							dto.setName("Доспех +1 (полулаты)");
							dto.setRarity(Rarity.UNCOMMON.getCyrilicName());
							dto.setCost(getCost(Rarity.UNCOMMON) + 750);
							break;
						case 46:
						case 47:
							dto.setName("Доспех +1 (чешуйчатый)");
							dto.setRarity(Rarity.RARE.getCyrilicName());
							dto.setCost(getCost(Rarity.UNCOMMON) + 50);
							break;
						case 48:
						case 49:
							dto.setName("Доспех +2 (кираса)");
							dto.setRarity(Rarity.RARE.getCyrilicName());
							dto.setCost(getCost(Rarity.RARE) + 400);
							break;
						case 50:
						case 51:
							dto.setName("Доспех +2 (наборной)");
							dto.setRarity(Rarity.RARE.getCyrilicName());
							dto.setCost(getCost(Rarity.RARE) + 200);
							break;
						case 52:
						case 53:
							dto.setName("Доспех +2 (проклёпанная кожа)");
							dto.setRarity(Rarity.RARE.getCyrilicName());
							dto.setCost(getCost(Rarity.RARE) + 45);
							break;
						case 54:
						case 55:
							dto.setName("Доспех +3 (кожаный)");
							dto.setRarity(Rarity.VERY_RARE.getCyrilicName());
							dto.setCost(getCost(Rarity.VERY_RARE) + 10);
							break;
						case 56:
						case 57:
							dto.setName("Доспех +3 (кольчуга)");
							dto.setRarity(Rarity.VERY_RARE.getCyrilicName());
							dto.setCost(getCost(Rarity.VERY_RARE) + 75);
							break;
						case 58:
						case 59:
							dto.setName("Доспех +3 (кольчужная рубаха)");
							dto.setRarity(Rarity.VERY_RARE.getCyrilicName());
							dto.setCost(getCost(Rarity.VERY_RARE) + 50);
							break;
						case 76:
							switch (1 + rnd.nextInt(12)) {
							case 1:
							case 2:
								dto.setName("Доспех +2 (полулаты)");
								dto.setRarity(Rarity.RARE.getCyrilicName());
								dto.setCost(getCost(Rarity.RARE) + 750);
								break;
							case 3:
							case 4:
								dto.setName("Доспех +2 (латы)");
								dto.setRarity(Rarity.RARE.getCyrilicName());
								dto.setCost(getCost(Rarity.RARE) + 1500);
								break;
							case 5:
							case 6:
								dto.setName("Доспех +3 (проклёпанная кожа)");
								dto.setRarity(Rarity.VERY_RARE.getCyrilicName());
								dto.setCost(getCost(Rarity.VERY_RARE) + 750);
								break;
							case 7:
							case 8:
								dto.setName("Доспех +3 (кираса)");
								dto.setRarity(Rarity.VERY_RARE.getCyrilicName());
								dto.setCost(getCost(Rarity.VERY_RARE) + 750);
								break;
							case 9:
							case 10:
								dto.setName("Доспех +3 (набороной)");
								dto.setRarity(Rarity.VERY_RARE.getCyrilicName());
								dto.setCost(getCost(Rarity.VERY_RARE) + 750);
								break;
							case 11:
								dto.setName("Доспех +3 (полулаты)");
								dto.setRarity(Rarity.VERY_RARE.getCyrilicName());
								dto.setCost(getCost(Rarity.VERY_RARE) + 750);
								break;
							case 12:
								dto.setName("Доспех +3 (латы)");
								dto.setRarity(Rarity.VERY_RARE.getCyrilicName());
								dto.setCost(getCost(Rarity.VERY_RARE) + 1550);
								break;
							}
							break;
						}
					}
					if (dto.getName().contains("Боеприпасы")) {
						int rb = rnd.nextInt(12);
						if (rb <= 6) {
							dto.setName(dto.getName() + " (стрелы)");
						} else if (rb < 9) {
							dto.setName(dto.getName() + " (болты)");
						} else if (rb < 10) {
							dto.setName(dto.getName() + " (дротики)");
						} else if (rb < 11) {
							dto.setName(dto.getName() + " (снаряды для пращи)");
						}
					} else if (dto.getName().contains("Свиток Заклинания")) {
						if (dto.getName().contains("заговор")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 0, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							dto.setId(spell.getId());
							dto.setEnglishSpellName(spell.getEnglishName());
							dto.setName("Свиток Заклинания " + "(Заговор: " + spell.getName().toLowerCase() + ")");
						}
						if (dto.getName().contains("1")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 1, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							dto.setId(spell.getId());
							dto.setEnglishSpellName(spell.getEnglishName());
							dto.setName("Свиток Заклинания " + "(1-ый уровень: " + spell.getName().toLowerCase() + ")");
						}
						if (dto.getName().contains("2")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 2, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							dto.setId(spell.getId());
							dto.setEnglishSpellName(spell.getEnglishName());
							dto.setName("Свиток Заклинания " + "(2-ой уровень: " + spell.getName().toLowerCase() + ")");
						}
						if (dto.getName().contains("3")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 3, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							dto.setId(spell.getId());
							dto.setEnglishSpellName(spell.getEnglishName());
							dto.setName("Свиток Заклинания " + "(3-ий уровень: " + spell.getName().toLowerCase() + ")");
						}
						if (dto.getName().contains("4")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 4, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							dto.setId(spell.getId());
							dto.setEnglishSpellName(spell.getEnglishName());
							dto.setName("Свиток Заклинания " + "(4-ый уровень: " + spell.getName().toLowerCase() + ")");
						}
						if (dto.getName().contains("5")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 5, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							dto.setId(spell.getId());
							dto.setEnglishSpellName(spell.getEnglishName());
							dto.setName("Свиток Заклинания " + "(5-ый уровень: " + spell.getName().toLowerCase() + ")");
						}
						if (dto.getName().contains("6")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 6, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							dto.setId(spell.getId());
							dto.setEnglishSpellName(spell.getEnglishName());
							dto.setName("Свиток Заклинания " + "(6-ой уровень: " + spell.getName().toLowerCase() + ")");
						}
						if (dto.getName().contains("7")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 7, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							dto.setId(spell.getId());
							dto.setEnglishSpellName(spell.getEnglishName());
							dto.setName("Свиток Заклинания " + "(7-ой уровень: " + spell.getName().toLowerCase() + ")");
						}
						if (dto.getName().contains("8")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 8, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							dto.setId(spell.getId());
							dto.setEnglishSpellName(spell.getEnglishName());
							dto.setName("Свиток Заклинания " + "(8-ой уровень: " + spell.getName().toLowerCase() + ")");
						}
						if (dto.getName().contains("9")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 9, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							dto.setId(spell.getId());
							dto.setEnglishSpellName(spell.getEnglishName());
							dto.setName("Свиток Заклинания " + "(9-ый уровень: " + spell.getName().toLowerCase() + ")");
						}
					} else if (dto.getName().contains("Оружие")) {
						List<Weapon> weapons = weaponRepo.findAll();
						Weapon weapon = weapons.get(rnd.nextInt(weapons.size()));
						dto.setName(dto.getName() + " (" + weapon.getName().toLowerCase() + ")");
					} else if (tableName.equals("Е1") && ri >= 12 && ri <= 14) {
						switch (1 + rnd.nextInt(8)) {
						case 1:
							dto.setName("Статуэтка чудесной силы " + "(Бронзовый грифон)");
							break;
						case 2:
							dto.setName("Статуэтка чудесной силы " + "(Эбеновая муха)");
							break;
						case 3:
							dto.setName("Статуэтка чудесной силы " + "(Золотые львы)");
							break;
						case 4:
							dto.setName("Статуэтка чудесной силы " + "(Костяные козлы)");
							break;
						case 5:
							dto.setName("Статуэтка чудесной силы " + "(Мраморный слон)");
							break;
						case 6:
						case 7:
							dto.setName("Статуэтка чудесной силы " + "(Ониксовая собака)");
							break;
						case 8:
							dto.setName("Статуэтка чудесной силы " + "(Серпентиновая сова)");
							break;
						}
					} else if (dto.getName().contains("Зелье Сопротивления")) {
						String resistType = getResistenceType();
						switch (resistType) {
						case "(звуку)":
							dto.setId(86);
							break;
						case "(излучению)":
							dto.setId(87);
							break;
						case "(кислоте)":
							dto.setId(421);
							break;
						case "(огню)":
							dto.setId(420);
							break;
						case "(некротической энергии)":
							dto.setId(422);
							break;
						case "(психической энергии)":
							dto.setId(423);
							break;
						case "(силовому полю)":
							dto.setId(424);
							break;
						case "(холоду)":
							dto.setId(425);
							break;
						case "(электричеству)":
							dto.setId(426);
							break;
						case "(яду)":
							//dto.setId(426);
							break;
						}
						dto.setName("Зелье Сопротивления" + " " + resistType);
					} else if (dto.getName().contains("Доспех Сопротивления")) {
						dto.setName(dto.getName() + " " + getResistenceType());
					}
					list.add(dto);
				}
			}
		}
		return list;
	}

	private String getResistenceType() {
		switch (1 + rnd.nextInt(10)) {
		case 1:
			return "(звуку)";
		case 2:
			return "(излучению)";
		case 3:
			return "(кислоте)";
		case 4:
			return "(некротической энергии)";
		case 5:
			return "(огню)";
		case 6:
			return "(психической энергии)";
		case 7:
			return "(силовому полю)";
		case 8:
			return "(холоду)";
		case 9:
			return "(электричеству)";
		case 10:
			return "(яду)";
		}
		return "";
	}

	private int getCost(Rarity rarity) {
		switch (rarity) {
		case COMMON:
			return (2 + rnd.nextInt(6)) * 10;
		case UNCOMMON:
			return (2 + rnd.nextInt(6)) * 100;
		case RARE:
			return (2 + rnd.nextInt(10) + rnd.nextInt(10)) * 1000;
		case VERY_RARE:
			return (2 + rnd.nextInt(4)) * 10000;
		case LEGENDARY:
			return (2 + rnd.nextInt(6) + rnd.nextInt(6)) * 25000;
		default:
			return 0;
		}
	}
}