package club.dnd5.portal.dto.api.races;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApiDto;
import club.dnd5.portal.dto.api.classes.NameApi;
import club.dnd5.portal.dto.api.classes.SourceTypeApi;
import club.dnd5.portal.model.races.Race;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class RaceApi {
	private NameApi name;
	private String url;
	private String abilities;
	private SourceTypeApi type;
	private SourceApiDto source;

	private List<RaceApi> subraces;
	private String icon;

	public RaceApi(Race race) {
		name = new NameApi(race.getCapitalazeName(), race.getEnglishName());
		if (race.getParent() == null) {
			url = String.format("/races/%s", race.getUrlName());
		}
		else {
			url = String.format("/races/%s/%s", race.getParent().getUrlName(), race.getUrlName());
		}
		source = new SourceApiDto(race.getBook());
		if (!race.getSubRaces().isEmpty()) {
			subraces = race.getSubRaces().stream().map(RaceApi::new).collect(Collectors.toList());
		}
		type = new SourceTypeApi(race.getBook().getType().getName(), race.getBook().getType().ordinal());
		abilities = race.getAbilityBonuses();
		icon = String.format("race-%s", race.getEnglishName().replace(' ', '-').toLowerCase());
	}
}