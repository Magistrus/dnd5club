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
	private String image;
	private Boolean icon;

	public ClassApi(HeroClass heroClass, ClassRequestApi request) {
		name = new NameApi(heroClass.getCapitalazeName(), heroClass.getEnglishName());
		url = String.format("/classes/%s", heroClass.getUrlName());
		source = new SourceApi(heroClass.getBook());
		dice = String.format("к%d", heroClass.getDiceHp());
		if (request.getSearch() != null && request.getSearch().getValue() != null
				&& !request.getSearch().getValue().isEmpty()) {
			archetypes = heroClass.getArchetypes().stream()
					.filter(a -> a.getName().toUpperCase().contains((request.getSearch().getValue().toUpperCase())) || a
							.getEnglishName().toUpperCase().contains((request.getSearch().getValue().toUpperCase())))
					.map(ArchetypeApiDto::new)
					.collect(Collectors.toList());
		} else {
			archetypes = heroClass.getArchetypes()
					.stream().map(ArchetypeApiDto::new)
					.collect(Collectors.toList());
		}
		if (heroClass.isSidekick()) {
			group = new GroupApi("Напарники", (byte) 0);
		}
		icon = !heroClass.isSidekick();
		image = String.format("class-%s", heroClass.getEnglishName().replace(' ', '-').toLowerCase());
	}
}