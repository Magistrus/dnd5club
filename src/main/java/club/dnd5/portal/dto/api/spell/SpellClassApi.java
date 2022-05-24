package club.dnd5.portal.dto.api.spell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.classes.archetype.Archetype;
import club.dnd5.portal.model.races.Race;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class SpellClassApi {
	private String name;
	private String source;
	private String url;
	private String icon;
	
	public SpellClassApi(HeroClass heroClass){
		name = heroClass.getCapitalazeName();
		url = String.format("/classes/%s", heroClass.getUrlName());
		icon = String.format("class-%s", heroClass.getEnglishName().replace(' ', '-').toLowerCase());
	}
	
	public SpellClassApi(Archetype archetype) {
		name = archetype.getCapitalizeName();
		url = String.format("/classes/%s/%s", archetype.getHeroClass().getUrlName(), archetype.getUrlName());
	}
	
	public SpellClassApi(Race race) {
		name = race.getCapitalazeName();
		url = String.format("/classes/%s", race.getUrlName());
	}
}