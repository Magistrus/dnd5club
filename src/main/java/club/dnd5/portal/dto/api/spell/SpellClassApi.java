package club.dnd5.portal.dto.api.spell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.classes.HeroClass;
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
}