package club.dnd5.portal.dto.api.races;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.NameValueApi;
import club.dnd5.portal.model.races.Feature;
import club.dnd5.portal.model.races.Race;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Setter
@Getter
public class RaceDetailApi extends RaceApi {
	private String description;
	private String size;
	private Collection<NameValueApi> speed = new ArrayList<>(5);
	private Collection<String> images;
	private Integer darkvision;
	
	public RaceDetailApi(Race race) {
		super(race);
		description = race.getDescription();
		url = null;
		type = race.getType().getCyrilicName();
		size = race.getSize().getCyrilicName();
		speed.add(new NameValueApi(null, race.getSpeed()));
		darkvision = race.getDarkvision();
	}

	protected void fillSkill(Race race) {
		if (race.getParent() != null) {
			final Set<Integer> replaceFeatureIds = race.getFeatures()
					.stream()
					.map(Feature::getReplaceFeatureId)
					.filter(Objects::nonNull)
					.collect(Collectors.toSet());
			List<RaceSkillApi> subraceSkills = race.getFeatures()
					.stream()
					.map(RaceSkillApi::new)
					.peek(api -> api.setSubrace(Boolean.TRUE))
					.collect(Collectors.toList());
			skill = race.getParent().getFeatures()
					.stream().filter(skill -> !replaceFeatureIds.contains(skill.getId()))
					.map(RaceSkillApi::new)
					.collect(Collectors.toList());
			skill.addAll(subraceSkills);
		} else {
			skill = race.getFeatures().stream().map(RaceSkillApi::new).collect(Collectors.toList());
		}
	}
}