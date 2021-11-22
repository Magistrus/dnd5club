package club.dnd5.portal.dto.item;


import club.dnd5.portal.model.items.Treasure;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TreasureDto {
	private int id;
	private String name;
	private String englishName;
	private int cost;
	private String type;
	private String book;
	private String bookshort;
	
	public TreasureDto(Treasure treasure) {
		id = treasure.getId();
		name = treasure.getName();
		englishName = treasure.getEnglishName();
		type = treasure.getType().getName();
		cost = treasure.getCost();
		book = treasure.getBook().getName();
		bookshort = treasure.getBook().getSource();
	}
}