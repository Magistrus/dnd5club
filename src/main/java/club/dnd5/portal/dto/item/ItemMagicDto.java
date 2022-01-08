package club.dnd5.portal.dto.item;

import java.util.Random;
import java.util.stream.Collectors;

import org.thymeleaf.util.StringUtils;

import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.items.Armor;
import club.dnd5.portal.model.items.MagicItem;
import club.dnd5.portal.model.items.Weapon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemMagicDto {
	private Integer id;
	private String name;
	private String englishName;
	private String englishSpellName;
	private String type;

	private String rarity;
	private String shortRarity;
	
	private String attunement;

	private String cost;
	private String book;
	private String bookshort;
	private String rarityEnglish;

	public ItemMagicDto(MagicItem magicItem) {
		id = magicItem.getId();
		name = StringUtils.capitalizeWords(magicItem.getName().toLowerCase()).replace(" И ", " и ")
				.replace(" Или ", " или ").replace(" За ", " за ").replace(" С ", " с ").replace(" На ", " на ")
				.replace(" От ", " от ").replace(" По ", " по ").replace(" Над ", " над ").replace(" В ", " в ")
				.replace(" Из ", " из ");
		englishName = magicItem.getEnglishName();
		type = magicItem.getType().getCyrilicName();
		if (!magicItem.getWeapons().isEmpty()) {
			type += " (" + magicItem.getWeapons().stream().map(Weapon::getName).map(String::toLowerCase)
					.collect(Collectors.joining(", ")) + ")";
		}
		if (!magicItem.getArmors().isEmpty()) {
			type += " (" +magicItem.getArmors().stream().map(Armor::getName).map(String::toLowerCase)
					.collect(Collectors.joining(", ")) + ")";
		}
		if (magicItem.getSpecial() != null) {
			type += " (" + magicItem.getSpecial() + ")";
		}
		rarity = magicItem.getRarity().getCyrilicName();
		rarityEnglish = magicItem.getRarity().name().toLowerCase();
		switch ( magicItem.getRarity()) {
		case COMMON:
			shortRarity = "О";
			break;
		case UNCOMMON:
			shortRarity = "Н";
			break;
		case RARE:
			shortRarity = "Р";
			break;
		case VERY_RARE:
			shortRarity = "ОР";
			break;
		case LEGENDARY:
			shortRarity = "Л";
			break;
		case ARTIFACT:
			shortRarity = "А";
			break;
		default:
			shortRarity = "-";
			break;
		}
		attunement = magicItem.getCustomization() ? "требуется настройка" : "Нет";
		if (magicItem.getCustSpecial() != null) {
			attunement += " (" + magicItem.getCustSpecial() + ")";
		}
		
		attunement += " " + magicItem.getCustClasses().stream().map(HeroClass::getAblativeName).map(String::toLowerCase)
					.collect(Collectors.joining(", "));
		String rangeCost;
		switch (magicItem.getRarity()) {
		case COMMON:
			if (magicItem.isConsumed()) {
				rangeCost = "от 25 до 50 зм.";
			} else {
				rangeCost = "от 50 до 100 зм.";
			}
			break;
		case UNCOMMON:
			if (magicItem.isConsumed()) {
				rangeCost = "от 51 до 250 зм.";
			} else {
				rangeCost = "от 101 до 500 зм.";
			}
			break;
		case RARE:
			rangeCost = "от 251 до 2 500 зм";
			if (magicItem.isConsumed()) {
				rangeCost = "от 501 до 5 000 зм";
			}
			break;
		case VERY_RARE:
			rangeCost = "от 2 501 до 25 000 зм.";
			if (magicItem.isConsumed()) {
				rangeCost = "от 5 001 до 50 000 зм.";
			}
			break;
		case LEGENDARY:
			rangeCost = "от 25 001 до 125 000 зм.";
			if (magicItem.isConsumed()) {
				rangeCost = "от 50 001 до 250 000 зм.";
			}
			break;
		case ARTIFACT:
			rangeCost = "от 250 001 зм. до невозможно купить";
			break;
		default:
			rangeCost = Integer.toString(magicItem.getCost());
			break;
		}
		cost = rangeCost;
		book = magicItem.getBook().getName();
		bookshort = magicItem.getBook().getSource();
	}

	public int getRandomCost() {
		Random rnd = new Random();
		switch (rarity) {
		case "обычный":
			return (rnd.nextInt(6) + 2) * 10;
		case "необычный":
			return (rnd.nextInt(6) + 2) * 100;
		case "редкий":
			return (rnd.nextInt(10) + rnd.nextInt(10) + 2) * 1000;
		case "очень редкий":
			return (rnd.nextInt(4) + 2) * 10_000;
		case "легендарный":
			return (rnd.nextInt(6) + rnd.nextInt(6) + 2) * 25000;
		default:
			return 300000*(1 + rnd.nextInt(10));
		}
	}

	public void setCost(int cost) {
		this.cost = String.valueOf(cost); 
	}
}