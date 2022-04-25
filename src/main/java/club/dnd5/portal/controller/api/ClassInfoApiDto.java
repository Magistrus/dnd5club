package club.dnd5.portal.controller.api;

import club.dnd5.portal.dto.api.classes.NameApiDto;
import club.dnd5.portal.model.classes.HeroClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassInfoApiDto {
	private NameApiDto name;
	public ClassInfoApiDto(HeroClass heroClass) {
		name = new NameApiDto(heroClass.getCapitalazeName(), heroClass.getEnglishName(), heroClass.getEnglishName().replace(' ', '_'));
	}
}