package club.dnd5.portal.dto.api.classes;

import club.dnd5.portal.dto.api.SourceApiDto;
import club.dnd5.portal.model.classes.archetype.Archetype;
import lombok.Getter;

@Getter
public class ArchetypeApiDto {
	private NameApiDto name;
	private SourceTypeApiDto type;
	private SourceApiDto source;
	private String url;

	public ArchetypeApiDto(Archetype archetype) {
		name = new NameApiDto(archetype.getCapitalizeName(), archetype.getEnglishName());
		type = new SourceTypeApiDto(archetype.getBook().getType().getName(), archetype.getBook().getType().ordinal());
		source = new SourceApiDto(archetype.getBook());
		url = String.format("/classes/%s/%s", archetype.getHeroClass().getUrlName(), archetype.getUrlName());
	}
}