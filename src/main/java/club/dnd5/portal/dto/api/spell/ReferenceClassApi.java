package club.dnd5.portal.dto.api.spell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.classes.Option.OptionType;
import club.dnd5.portal.model.classes.archetype.Archetype;
import club.dnd5.portal.model.races.Race;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class ReferenceClassApi {
	private String name;
	private String source;
	private String url;
	private String icon;
    @JsonProperty("class")
	private String parent;
	
	public ReferenceClassApi(HeroClass heroClass){
		name = heroClass.getCapitalazeName();
		url = String.format("/classes/%s", heroClass.getUrlName());
		icon = String.format("class-%s", heroClass.getEnglishName().replace(' ', '-').toLowerCase());
	}
	
	public ReferenceClassApi(Archetype archetype) {
		name = archetype.getCapitalizeName();
		url = String.format("/classes/%s/%s", archetype.getHeroClass().getUrlName(), archetype.getUrlName());
		parent = archetype.getHeroClass().getCapitalazeName();
	}
	
	public ReferenceClassApi(Race race) {
		name = race.getCapitalazeName();
		url = String.format("/races/%s", race.getUrlName());
	}
	
	public ReferenceClassApi(OptionType optionType) {
		name = optionType.getName();
		if (optionType.getArhetypeName() != null) {
			url = String.format("/classes/%s/%s", optionType.getArhetypeName().replace(' ', '_'), optionType.getClassName().replace(' ', '_'));
		} else {
			url = String.format("/classes/%s", optionType.getClassName().replace(' ', '_'));
		}
		icon = String.format("class-%s", optionType.getClassName().replace(' ', '-').toLowerCase());
	}
}