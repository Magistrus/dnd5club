package club.dnd5.portal.dto.api.races;

import java.util.List;

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
	private List<String> ability;
	private String size;
	public RaceDetailApi(Race race) {
		super(race);
		url = null;
		sourceApi = new SourceApi(race.getBook());
		type = race.getType().getCyrilicName();
		size = race.getSize().getCyrilicName();
	}
}