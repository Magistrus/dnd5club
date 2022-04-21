package club.dnd5.portal.dto.api.classes;

import java.util.List;
import java.util.stream.Collectors;

import club.dnd5.portal.dto.api.SourceApiDto;
import club.dnd5.portal.model.classes.HeroClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassApiDto {
	private NameApiDto name;
	private SourceApiDto source;
	private String dice;
	private List<ArchetypeApiDto> archetypes;
	public ClassApiDto(HeroClass heroClass) {
		name = new NameApiDto(heroClass.getCapitalazeName(), heroClass.getEnglishName(), String.format("/classes/%s", heroClass.getEnglishName().replace(' ', '_')));
		source = new SourceApiDto(heroClass.getBook());
		dice = String.format("к%d", heroClass.getDiceHp());
		archetypes = heroClass.getArchetypes().stream().map(ArchetypeApiDto::new).collect(Collectors.toList());
	}
}