package club.dnd5.portal.dto;

import club.dnd5.portal.model.screen.Screen;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScreenDto {
	private int id;
	private String name;
	private String englishName;
	private boolean parent;
	public ScreenDto(Screen screen) {
		id = screen.getId();
		name = screen.getName();
		englishName = screen.getEnglishName();
		parent = screen.getChields().isEmpty();	
	}
}