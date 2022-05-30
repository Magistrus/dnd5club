package club.dnd5.portal.dto.api.classes;

import club.dnd5.portal.dto.api.SourceApiDto;
import club.dnd5.portal.model.classes.archetype.Archetype;
import lombok.Getter;

@Getter
public class ArchetypeApiDto {
	private NameApi name;
	private SourceTypeApi type;
	private SourceApiDto source;
	private String url;

	public ArchetypeApiDto(Archetype archetype) {
		name = new NameApi(archetype.getHeroClass().getCapitalazeName() + " " + archetype.getCapitalizeName(), archetype.getEnglishName() + " " + archetype.getEnglishName());
		type = new SourceTypeApi(archetype.getBook().getType().getName(), archetype.getBook().getType().ordinal());
		source = new SourceApiDto(archetype.getBook());
		url = String.format("/classes/%s/%s", archetype.getHeroClass().getUrlName(), archetype.getUrlName());
	}
}