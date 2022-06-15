package club.dnd5.portal.dto.api.races;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.NameValueApi;
import club.dnd5.portal.dto.api.SourceApi;
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
	protected String url;
	private Collection<NameValueApi> abilities;
	protected Object type;
	private SourceApi source;

	private List<RaceApi> subraces;
	protected Collection<RaceSkillApi> skill;

	private String icon;

	public RaceApi(Race race) {
		name = new NameApi(race.getCapitalazeName(), race.getEnglishName());
		if (race.getParent() == null) {
			url = String.format("/races/%s", race.getUrlName());
		}
		else {
			url = String.format("/races/%s/%s", race.getParent().getUrlName(), race.getUrlName());
		}
		source = new SourceApi(race.getBook());
		if (!race.getSubRaces().isEmpty()) {
			subraces = race.getSubRaces().stream().map(RaceApi::new).collect(Collectors.toList());
		}
		type = new SourceTypeApi(race.getBook().getType().getName(), race.getBook().getType().ordinal());
		abilities = race.getAbilityValueBonuses()
				.stream()
				.map(bonus -> new NameValueApi(bonus.getAbility().getCyrilicName(), bonus.getAbility().getShortName(), bonus.getBonus()))
				.collect(Collectors.toList());
		icon = String.format("race-%s", race.getEnglishName().replace(' ', '-').toLowerCase());
		fillSkill(race);
	}

	protected void fillSkill(Race race) {
		
	}
}