package club.dnd5.portal.dto.api.classes;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApiDto;
import club.dnd5.portal.model.races.Race;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class RaceApiDto {
	private NameApiDto name;
	private String abilities;
	private SourceTypeApiDto type;
	private SourceApiDto source;

	private List<RaceApiDto> subraces;
	private String icon;

	public RaceApiDto(Race race) {
		name = new NameApiDto(race.getCapitalazeName(), race.getEnglishName());
		source = new SourceApiDto(race.getBook());
		if (!race.getSubRaces().isEmpty()) {
			subraces = race.getSubRaces().stream().map(RaceApiDto::new).collect(Collectors.toList());
		}
		type = new SourceTypeApiDto(race.getBook().getType().getName(), race.getBook().getType().ordinal());
		abilities = race.getAbilityBonuses();
		icon = String.format("race-%s", race.getEnglishName().replace(' ', '-'));
	}
}