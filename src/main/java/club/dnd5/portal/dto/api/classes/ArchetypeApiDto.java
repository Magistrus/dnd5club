package club.dnd5.portal.dto.api.classes;

import club.dnd5.portal.dto.api.SourceApiDto;
import club.dnd5.portal.model.classes.archetype.Archetype;
import lombok.Getter;

@Getter
public class ArchetypeApiDto {
	private NameApiDto name;
	private String type;
	private SourceApiDto source;

	public ArchetypeApiDto(Archetype archetype) {
		name = new NameApiDto(archetype.getCapitalizeName(), archetype.getEnglishName(),
				String.format("/classes/%s/%s", archetype.getHeroClass().getEnglishName().replace(' ', '_'),
						archetype.getEnglishName().replace(' ', '_')));
		type = archetype.getBook().getType().getName();
		source = new SourceApiDto(archetype.getBook());
	}
}