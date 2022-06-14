package club.dnd5.portal.dto.api.races;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.NameValueApi;
import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.model.races.Feature;
import club.dnd5.portal.model.races.Race;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Setter
@Getter
public class RaceDetailApi extends RaceApi {
	private SourceApi sourceApi;
	private String size;
	private Collection<NameValueApi> speed = new ArrayList<>(5);
	private Collection<String> images;
	private Collection<RaceSkillApi> skill;
	
	public RaceDetailApi(Race race) {
		super(race);
		url = null;
		sourceApi = new SourceApi(race.getBook());
		type = race.getType().getCyrilicName();
		size = race.getSize().getCyrilicName();
		speed.add(new NameValueApi(null, race.getSpeed()));
		if (race.getParent() != null) {
			skill = race.getParent().getFeatures().stream().map(RaceSkillApi::new).collect(Collectors.toList());
			skill.addAll(race.getFeatures().stream().map(RaceSkillApi::new).peek(api -> api.setSubrace(Boolean.TRUE)).collect(Collectors.toList()));
		} else {
			skill = race.getFeatures().stream().map(RaceSkillApi::new).collect(Collectors.toList());
		}
	}
}