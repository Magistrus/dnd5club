package club.dnd5.portal.dto.api.races;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.model.races.Race;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Setter
@Getter
public class RaceDetailApi extends RaceApi {
	private SourceApi sourceApi;
	private Collection<String> ability;
	private String size;
	private Collection<String> images;
	
	public RaceDetailApi(Race race) {
		super(race);
		url = null;
		sourceApi = new SourceApi(race.getBook());
		type = race.getType().getCyrilicName();
		size = race.getSize().getCyrilicName();
	}
}