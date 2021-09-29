package club.dnd5.portal.dto.item;

import club.dnd5.portal.model.items.Armor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArmorDto {
	private int id;
	private String name;
	private String englishName;
	private String cost;
	private String ac;
	private String weight;
	private String type;
	private String requirements;
	private String stealth;
	private String book;
	private String bookshort;
	
	public ArmorDto(Armor armor) {
		id = armor.getId();
		name = armor.getName();
		englishName = armor.getEnglishName();
		cost = armor.getCost() + " зм.";
		weight = String.valueOf(armor.getWeight());
		ac = String.valueOf(armor.getAC());
		type = armor.getType().getName();
		requirements =  String.valueOf(armor.getForceRequirements() == null ? "Нет":armor.getForceRequirements());
		stealth = armor.isStelsHindrance() ? "Есть" : "Нет";
		book = armor.getBook().getName();
		bookshort = armor.getBook().getSource(); 
	}
}