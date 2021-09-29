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
	private String type;

	private String rarity;
	private String shortRarity;
	
	private String attunement;

	private String cost;
	private String book;
	private String bookshort;

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
		attunement += magicItem.getCustSpecial() != null ? " " + magicItem.getCustSpecial() : "";
		attunement += " " + magicItem.getCustClasses().stream().map(HeroClass::getAblativeName).map(String::toLowerCase)
					.collect(Collectors.joining(", "));
		String rangeCost;
		switch (magicItem.getRarity()) {
		case COMMON:
			if (magicItem.isConsumed()) {
				rangeCost = "от 10 до 35 зм.";
			} else {
				rangeCost = "от 20 до 70 зм.";
			}
			break;
		case UNCOMMON:
			if (magicItem.isConsumed()) {
				rangeCost = "от 100 до 350 зм.";
			} else {
				rangeCost = "от 200 до 700 зм.";
			}
			break;
		case RARE:
			rangeCost = "от 2 000 до 20 000 зм";
			if (magicItem.isConsumed()) {
				rangeCost = "от 1 000 до 10 000 зм";
			}
			break;
		case VERY_RARE:
			rangeCost = "от 20 000 до 50 000 зм.";
			if (magicItem.isConsumed()) {
				rangeCost = "от 10 000 до 25 000 зм.";
			}
			break;
		case LEGENDARY:
			rangeCost = "от 50 000 до 300 000 зм.";
			if (magicItem.isConsumed()) {
				rangeCost = "от 25 000 до 150 000 зм.";
			}
			break;
		case ARTIFACT:
			rangeCost = "от 300 000 зм. до невозможно купить";
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