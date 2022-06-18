package club.dnd5.portal.controller.api.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.NameValueApi;
import club.dnd5.portal.dto.api.item.MagicItemApi;
import club.dnd5.portal.dto.api.spell.SpellApi;
import club.dnd5.portal.dto.api.tools.RequestTraderApi;
import club.dnd5.portal.model.Alignment;
import club.dnd5.portal.model.Dice;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.items.MagicThingTable;
import club.dnd5.portal.model.items.Rarity;
import club.dnd5.portal.model.items.Weapon;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.datatable.ItemMagicTableRepository;
import club.dnd5.portal.repository.datatable.MagicItemDatatableRepository;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;
import club.dnd5.portal.repository.datatable.WeaponDatatableRepository;

@RestController
public class TraderApicontroller {
	private static final Random rnd = new Random();
	@Autowired
	private MagicItemDatatableRepository magicItemRepo;
	@Autowired
	private SpellDatatableRepository spellRepo;
	@Autowired
	private WeaponDatatableRepository weaponRepo;
	@Autowired
	private ItemMagicTableRepository mtRepo;
	
	@GetMapping("/api/v1/tools/trader")
	public List<NameValueApi> getMagicLevels(){
		List<NameValueApi> magicLevels = new ArrayList<>(3);
		magicLevels.add(new NameValueApi("Мало", 0));
		magicLevels.add(new NameValueApi("Норма", 0));
		magicLevels.add(new NameValueApi("Много", 0));
		return magicLevels;
	}
	
	@PostMapping("/api/v1/tools/trader")
	public List<MagicItemApi> getItems(RequestTraderApi reques){
		int coef = 0;
		if (reques.getMagicLevel() == 1) {
			coef = -10;
		} else if (reques.getMagicLevel() == 3) {
			coef = 10;
		}
		List<MagicItemApi> list = new ArrayList<>();
		list.addAll(getMagicThings(reques.getPersuasion() + coef, 1, 5, "А", 6));
		list.addAll(getMagicThings(reques.getPersuasion() + coef, 6, 10, "Б", 4));
		list.addAll(getMagicThings(reques.getPersuasion() + coef, 11, 15, "В", 4));
		list.addAll(getMagicThings(reques.getPersuasion() + coef, 16, 20, "Г", 4));
		list.addAll(getMagicThings(reques.getPersuasion() + coef, 21, 25, "Д", 4));
		list.addAll(getMagicThings(reques.getPersuasion() + coef, 26, 30, "Е", 4));
		list.addAll(getMagicThings(reques.getPersuasion() + coef, 31, 35, "Е1", 4));
		list.addAll(getMagicThings(reques.getPersuasion() + coef, 36, 40, "Ж", 4));
		list.addAll(getMagicThings(reques.getPersuasion() + coef, 41, 1000, "З", 4));
		return list;
	}
	
	private List<MagicItemApi> getMagicThings(Integer persuasion, int start, int end, String tableName, int count) {
		if (persuasion == null) {
			persuasion = 1;
		}
		List<MagicItemApi> list = new ArrayList<>();
		if (persuasion >= start) {
			for (int i = 0; i < 1 + rnd.nextInt(count); i++) {
				int ri = Dice.roll(Dice.d100);
				// System.out.println("table= " + tableName + " in " + ri);
				MagicThingTable mt = mtRepo.findOne(ri, tableName);
				if (mt != null) {
					MagicItemApi itemApi = new MagicItemApi(mt.getMagicThing());
					itemApi.setCost(getCost(mt.getMagicThing().getRarity()));
					if (tableName.equals("Б")) {
						if (ri == 91) {
							int zap = Dice.roll(4, Dice.d4);
							itemApi.changeName("(дополнительных заплаток " + zap + ")");
						}
					}
					if (tableName.equals("В")) {
						if (ri >= 82 && ri <= 84) {
							String effect = "";
							int er = Dice.roll(Dice.d100);
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
							itemApi.changeName("(Эффект: " + effect + ")");
						} else if (ri >= 85 && ri <= 87) {
							String effect = "";
							int er = Dice.roll(Dice.d100);
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
							itemApi.changeName("(Вид существ: " + effect + ")");
						} else if (ri >= 88 && ri <= 89) {
							itemApi.changeName(String.format("(Бобов: %d)", Dice.roll(2, Dice.d4)));
						}
					}
					if (tableName.equals("E")) {
						switch (ri) {
						case 66:
							itemApi.changeName("(кольчуга)");
							itemApi.setCost(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 75);
							break;
						case 67:
							itemApi.changeName("Адамантиновый Доспех (кольчужная рубаха)");
							itemApi.setCost(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 50);
							break;
						case 68:
							itemApi.changeName("Адамантиновый Доспех (чещуйчатый доспех)");
							itemApi.setCost(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 50);
							break;
						}
					}
					if (tableName.equals("E1")) {
						switch (ri) {
						case 15:
							itemApi.changeName("(кираса)");
							itemApi.setCost(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 400);
							break;
						case 16:
							itemApi.changeName("(наборной доспех)");
							itemApi.setCost(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 200);
							break;
						case 35:
							itemApi.changeName("(кожаный)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							itemApi.setCost(getCost(Rarity.UNCOMMON) + 10);
							break;
						case 36:
							itemApi.changeName("(кольчуга)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							itemApi.setCost(getCost(Rarity.UNCOMMON) + 75);
							break;
						case 37:
							itemApi.changeName("(кольчужная рубаха)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							itemApi.setCost(getCost(Rarity.UNCOMMON) + 50);
							break;
						case 38:
							itemApi.changeName("(чешуйчатый)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							itemApi.setCost(getCost(Rarity.UNCOMMON) + 50);
							break;
						case 60:
							itemApi.changeName(getResistenceType());
							break;
						}
					}
					if (tableName.equals("Ж")) {
						switch (ri) {
						case 55:
							itemApi.changeName("(латы)");
							itemApi.setCost(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 1500);
							break;
						case 56:
							itemApi.changeName("(полулаты)");
							itemApi.setCost(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 750);
							break;
						case 65:
							itemApi.changeName("(кираса)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							itemApi.setCost(getCost(Rarity.UNCOMMON) + 400);
							break;
						case 66:
							itemApi.changeName("(кираса)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							itemApi.setCost(getCost(Rarity.UNCOMMON) + 400);
							break;
						case 67:
							itemApi.changeName("(проклёпанная кожа)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							itemApi.setCost(getCost(Rarity.UNCOMMON) + 400);
							break;
						case 68:
							itemApi.changeName("(кожаный)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							itemApi.setCost(getCost(Rarity.RARE) + 10);
							break;
						case 69:
							itemApi.changeName("(кольчуга)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							itemApi.setCost(getCost(Rarity.RARE) + 75);
							break;
						case 70:
							itemApi.changeName("(кольчужная рубаха)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							itemApi.setCost(getCost(Rarity.RARE) + 50);
							break;
						case 71:
							itemApi.changeName("(чешуйчатый)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							itemApi.setCost(getCost(Rarity.RARE) + 50);
							break;
						case 89:
							String aligment = Alignment.values()[rnd.nextInt(Alignment.values().length)]
									.getCyrilicName();
							itemApi.changeName("(Мировоззрение: " + aligment + ")");
							break;
						case 91:
							int rg = Dice.roll(Dice.d20);
							String golemType = "";
							if (rg >= 1 && rg <= 5)
								golemType = "Глинянный";
							else if (rg == 6)
								golemType = "Железный";
							else if (rg >= 7 && rg <= 8)
								golemType = "Каменный";
							else if (rg >= 9 && rg <= 20)
								golemType = "Мясной (из плоти)";
							itemApi.changeName(String.format("(%s)", golemType));
							break;
						}
					}
					if (tableName.equals("З")) {
						switch (ri) {
						case 42:
						case 43:
							itemApi.changeName("(латы)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							itemApi.setCost(getCost(Rarity.UNCOMMON) + 1500);
							break;
						case 44:
						case 45:
							itemApi.changeName("(полулаты)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							itemApi.setCost(getCost(Rarity.UNCOMMON) + 750);
							break;
						case 46:
						case 47:
							itemApi.changeName("(чешуйчатый)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							itemApi.setCost(getCost(Rarity.UNCOMMON) + 50);
							break;
						case 48:
						case 49:
							itemApi.changeName("(кираса)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							itemApi.setCost(getCost(Rarity.RARE) + 400);
							break;
						case 50:
						case 51:
							itemApi.changeName("(наборной)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							itemApi.setCost(getCost(Rarity.RARE) + 200);
							break;
						case 52:
						case 53:
							itemApi.changeName("(проклёпанная кожа)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							itemApi.setCost(getCost(Rarity.RARE) + 45);
							break;
						case 54:
						case 55:
							itemApi.changeName("(кожаный)");
							itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
							itemApi.setCost(getCost(Rarity.VERY_RARE) + 10);
							break;
						case 56:
						case 57:
							itemApi.changeName("(кольчуга)");
							itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
							itemApi.setCost(getCost(Rarity.VERY_RARE) + 75);
							break;
						case 58:
						case 59:
							itemApi.changeName("(кольчужная рубаха)");
							itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
							itemApi.setCost(getCost(Rarity.VERY_RARE) + 50);
							break;
						case 76:
							switch (Dice.roll(Dice.d12)) {
							case 1:
							case 2:
								itemApi.changeName("(полулаты)");
								itemApi.setRarity(Rarity.RARE.getCyrilicName());
								itemApi.setCost(getCost(Rarity.RARE) + 750);
								break;
							case 3:
							case 4:
								itemApi.changeName("(латы)");
								itemApi.setRarity(Rarity.RARE.getCyrilicName());
								itemApi.setCost(getCost(Rarity.RARE) + 1500);
								break;
							case 5:
							case 6:
								itemApi.changeName("(проклёпанная кожа)");
								itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
								itemApi.setCost(getCost(Rarity.VERY_RARE) + 750);
								break;
							case 7:
							case 8:
								itemApi.changeName("(кираса)");
								itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
								itemApi.setCost(getCost(Rarity.VERY_RARE) + 750);
								break;
							case 9:
							case 10:
								itemApi.changeName("(набороной)");
								itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
								itemApi.setCost(getCost(Rarity.VERY_RARE) + 750);
								break;
							case 11:
								itemApi.changeName("(полулаты)");
								itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
								itemApi.setCost(getCost(Rarity.VERY_RARE) + 750);
								break;
							case 12:
								itemApi.changeName("(латы)");
								itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
								itemApi.setCost(getCost(Rarity.VERY_RARE) + 1550);
								break;
							}
							break;
						}
					}
					if (itemApi.getName().getRus().contains("Боеприпасы")) {
						int rb = Dice.roll(Dice.d12);
						if (rb <= 6) {
							itemApi.changeName("(стрелы)");
						} else if (rb < 9) {
							itemApi.changeName("(болты)");
						} else if (rb < 10) {
							itemApi.changeName("(дротики)");
						} else if (rb < 11) {
							itemApi.changeName("(снаряды для пращи)");
						}
					} else if (itemApi.getName().getRus().contains("Свиток Заклинания")) {
						if (itemApi.getName().getRus().contains("заговор")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 0, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("1")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 1, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("2")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 2, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("3")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 3, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("4")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 4, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("5")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 5, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("6")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 6, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("7")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 7, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("8")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 8, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("9")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 9, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
					} else if (itemApi.getName().getRus().contains("Оружие")) {
						List<Weapon> weapons = weaponRepo.findAll();
						Weapon weapon = weapons.get(rnd.nextInt(weapons.size()));
						itemApi.changeName(" (" + weapon.getName().toLowerCase() + ")");
					} else if (tableName.equals("Е1") && ri >= 12 && ri <= 14) {
						switch (Dice.roll(Dice.d8)) {
						case 1:
							itemApi.changeName("(Бронзовый грифон)");
							break;
						case 2:
							itemApi.changeName("(Эбеновая муха)");
							break;
						case 3:
							itemApi.changeName("(Золотые львы)");
							break;
						case 4:
							itemApi.changeName("(Костяные козлы)");
							break;
						case 5:
							itemApi.changeName("(Мраморный слон)");
							break;
						case 6:
						case 7:
							itemApi.changeName("(Ониксовая собака)");
							break;
						case 8:
							itemApi.changeName("(Серпентиновая сова)");
							break;
						}
					} else if (itemApi.getName().getRus().contains("Зелье Сопротивления")) {
						String resistType = getResistenceType();
						switch (resistType) {
						case "(звуку)":
							itemApi = new MagicItemApi(magicItemRepo.findById(86).get());
							break;
						case "(излучению)":
							itemApi = new MagicItemApi(magicItemRepo.findById(87).get());
							break;
						case "(кислоте)":
							itemApi = new MagicItemApi(magicItemRepo.findById(421).get());
							break;
						case "(огню)":
							itemApi = new MagicItemApi(magicItemRepo.findById(420).get());
							break;
						case "(некротической энергии)":
							itemApi = new MagicItemApi(magicItemRepo.findById(422).get());
							break;
						case "(психической энергии)":
							itemApi = new MagicItemApi(magicItemRepo.findById(423).get());
							break;
						case "(силовому полю)":
							itemApi = new MagicItemApi(magicItemRepo.findById(424).get());
							break;
						case "(холоду)":
							itemApi = new MagicItemApi(magicItemRepo.findById(425).get());
							break;
						case "(электричеству)":
							itemApi = new MagicItemApi(magicItemRepo.findById(426).get());
							break;
						case "(яду)":
							itemApi = new MagicItemApi(magicItemRepo.findById(896).get());
							break;
						}
					} else if (itemApi.getName().getRus().contains("Доспех Сопротивления")) {
						itemApi.changeName(getResistenceType());
					}
					list.add(itemApi);
				}
			}
		}
		return list;
	}

	private String getResistenceType() {
		switch (Dice.roll(Dice.d10)) {
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
			return Dice.roll(2, Dice.d6) * 10;
		case UNCOMMON:
			return Dice.roll(2, Dice.d6) * 100;
		case RARE:
			return Dice.roll(2, Dice.d10) * 1000;
		case VERY_RARE:
			return Dice.roll(2, Dice.d4) * 10000;
		case LEGENDARY:
			return Dice.roll(2, Dice.d6) * 25000;
		default:
			return 0;
		}
	}
}