package club.dnd5.portal.dto.item;

import java.util.stream.Collectors;

import club.dnd5.portal.model.items.Equipment;
import club.dnd5.portal.model.items.EquipmentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemDto {
	private int id;
	private String name;
	private String englishName;
	private String cost;
	private String weight;
	private String type;
	private String book;
	private String bookshort;
	
	public ItemDto(Equipment equipment) {
		id = equipment.getId();
		name = equipment.getName();
		englishName = equipment.getEnglishName();
		if (equipment.getCost() == null) {
			cost = "&mdash;";
		}
		else
		{
			cost = equipment.getCost() + " " + equipment.getCurrency().getName();
			switch (equipment.getCurrency()) {
			case SM:
				cost=  String.valueOf(equipment.getCost() / 10f) + " " + equipment.getCurrency().getName();
				break;
			case GM:
				cost=  String.valueOf(equipment.getCost() / 100f) + " " + equipment.getCurrency().getName();
				break;
			case PM:
				cost=  String.valueOf(equipment.getCost() / 1000f) + " " + equipment.getCurrency().getName();
				break;
			default:
				break;
			}
		}
		weight = (equipment.getWeight() == null ? "&mdash;": String.valueOf(equipment.getWeight()));
		type = equipment.getTypes().stream().map(EquipmentType::getCyrilicName).collect(Collectors.joining(", "));
		book = equipment.getBook().getName();
		bookshort = equipment.getBook().getSource(); 
	}
}