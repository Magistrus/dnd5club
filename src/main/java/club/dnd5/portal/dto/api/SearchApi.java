package club.dnd5.portal.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.classes.NameApi;
import club.dnd5.portal.model.races.Race;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class SearchApi {
	private NameApi name;
	private String section;
	private String url;

	public SearchApi(Race race) {
		name = new NameApi(race.getCapitalazeName(), race.getEnglishName());
		section = "Расы";
		url = String.format("/races", race.getUrlName());
	}
}
