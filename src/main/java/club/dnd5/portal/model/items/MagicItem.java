package club.dnd5.portal.model.items;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	
	@Column
	private Integer cost;
	private Byte bonus;
	
	@OneToMany
	private List<HeroClass> custClasses;
	
	@OneToMany
	private List<Weapon> weapons;
	
	@OneToMany
	private List<Armor> armors;
	
	@OneToMany
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
}