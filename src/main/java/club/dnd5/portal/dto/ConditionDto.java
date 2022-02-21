package club.dnd5.portal.dto;

import club.dnd5.portal.model.Condition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionDto {
	private int id;
	private String name;
	private String englishName;
	private String type;
	private String book;
	private String bookshort;
	public ConditionDto(Condition condition) {
		id = condition.getId();
		name = condition.getName();
		englishName = condition.getEnglishName();
		type = condition.getType().getName();
		book = condition.getBook().getName();
		book = condition.getBook().getSource();
	}
}