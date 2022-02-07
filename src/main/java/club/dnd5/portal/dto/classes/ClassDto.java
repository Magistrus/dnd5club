package club.dnd5.portal.dto.classes;

import club.dnd5.portal.model.classes.HeroClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClassDto {
	private Integer id;
	private String name;
	private String englishName;
	
	public ClassDto(HeroClass hero) {
		id = hero.getId();
		name = hero.getName();
		englishName = hero.getEnglishName();
	}
}