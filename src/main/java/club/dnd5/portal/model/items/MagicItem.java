package club.dnd5.portal.model.items;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.classes.HeroClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "artifactes")
public class MagicItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String englishName;
	private String altName;

	@Enumerated(EnumType.ORDINAL)
	private Rarity rarity;

	@Enumerated(EnumType.ORDINAL)
	private MagicThingType type;

	private Boolean customization;

	@Column
	private String custSpecial;

	@Column
	private String special;

	@Column(columnDefinition = "TEXT")
	private String description;
	private boolean consumed;
	private Integer charge;

	@Column
	private Integer cost;
	private Byte bonus;

	@OneToMany
	@JoinTable(name = "artifactes_cust_classes")
	private List<HeroClass> custClasses;

	@OneToMany
	@JoinTable(name = "artifactes_weapons")
	private List<Weapon> weapons;

	@OneToMany
	@JoinTable(name = "artifactes_armors")
	private List<Armor> armors;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "magic_thing_id")
	private List<MagicThingTable> tables;

	@ManyToOne
	@JoinColumn(name = "source")
	private Book book;
	private Short page;

	public int getCost() {
		if (cost != null) {
			return cost;
		}
		return consumed ? rarity.getBaseCost() / 2 : rarity.getBaseCost();
	}

	public int getBaseCost() {
		return cost;
	}

	@Override
	public String toString() {
		return name.toLowerCase();
	}

	public String getRangeCostDMG() {
		switch (rarity) {
		case COMMON:
			if (isConsumed()) {
				return "от 25 до 50 зм.";
			} else {
				return "от 50 до 100 зм.";
			}
		case UNCOMMON:
			if (isConsumed()) {
				return "от 51 до 250 зм.";
			} else {
				return "от 101 до 500 зм.";
			}
		case RARE:
			if (isConsumed()) {
				return "от 251 до 2 500 зм";
			} else {
				return "от 501 до 5 000 зм";
			}
		case VERY_RARE:
			if (isConsumed()) {
				return "от 2 501 до 25 000 зм.";
			} else {
				return "от 5 001 до 50 000 зм.";
			}
		case LEGENDARY:
			if (isConsumed()) {
				return "от 25 001 до 125 000 зм.";
			} else {
				return "от 50 001 до 250 000 зм.";
			}
		case ARTIFACT:
			return "от 250 001 зм. до невозможно купить";
		default:
			return Integer.toString(getCost());
		}
	}

	public String getRangeCostXGE() {
		switch (rarity) {
		case COMMON:
			return "(1к6 + 1) * 10 зм.";
		case UNCOMMON:
			return "(1к6 + 1) * 100 зм.";
		case RARE:
			return "2к10 * 1000 зм.";
		case VERY_RARE:
			return "(1к4 + 1) * 10000 зм.";
		case LEGENDARY:
			return "2к6 * 25000 зм.";
		case ARTIFACT:
			return "невозможно купить";
		default:
			return Integer.toString(getCost());
		}
	}
	
	public String getTextRarity() {
		switch (type) {
		case AMMUNITION:
		case WAND:
			return rarity.getFemaleName();
		case POTION:
		case RING:
		case WEAPON:
		case MELE_WEAPON:
		case RANGED_WEAPON:
			return rarity.getMiddleName();
		default:
			return rarity.getCyrilicName();
		}
	}
}