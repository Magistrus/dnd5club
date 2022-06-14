package club.dnd5.portal.dto.api.classes;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.GroupApi;
import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.model.classes.HeroClass;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class ClassApi {
	private NameApi name;
	private String url;
	private SourceApi source;
	private String dice;
	private List<ArchetypeApiDto> archetypes;
	private GroupApi group;
	private String icon;

	public ClassApi(HeroClass heroClass) {
		name = new NameApi(heroClass.getCapitalazeName(), heroClass.getEnglishName());
		url = String.format("/classes/%s", heroClass.getUrlName());
		source = new SourceApi(heroClass.getBook());
		dice = String.format("к%d", heroClass.getDiceHp());
		archetypes = heroClass.getArchetypes().stream().map(ArchetypeApiDto::new).collect(Collectors.toList());
		if(heroClass.isSidekick())
		{
			group = new GroupApi("Напарники", (byte) 0);
		}
		icon = String.format("class-%s", heroClass.getEnglishName().replace(' ', '-').toLowerCase());
	}
}