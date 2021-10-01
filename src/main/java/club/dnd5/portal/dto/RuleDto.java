package club.dnd5.portal.dto;

import club.dnd5.portal.model.rule.Rule;
import lombok.Getter;

@Getter
public class RuleDto {
	private int id;
	private String name;
	private String englishName;
	private String type;
	private String book;
	private String bookshort;
	public RuleDto(Rule rule) {
		id = rule.getId();
		name = rule.getName();
		englishName = rule.getEnglishName();
		type = rule.getType();
		book = rule.getBook().getName(); 
		if (rule.getPage() != null)  {
			book += ", стр. " + rule.getPage();
		}
		bookshort = rule.getBook().getSource();
	}
}