package club.dnd5.portal.dto.api.classes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	private List<NamedListApi> archetypes;
	private GroupApi group;
	private String image;
	private String icon;

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
					.map(NamedListApi::new)
					.collect(Collectors.toList());
		} else {
			archetypes = heroClass.getArchetypes()
					.stream().map(NamedListApi::new)
					.collect(Collectors.toList());
		}
		if (heroClass.isSidekick()) {
			group = new GroupApi("Напарники", (byte) 0);
		}
		if (request.getFilter() !=null && request.getFilter().getBooks()!=null && !request.getFilter().getBooks().isEmpty()) {
			Set<String> books = new HashSet<>(request.getFilter().getBooks());
			archetypes = archetypes.stream().filter(a -> books.contains(a.getSource().getShortName())).collect(Collectors.toList());
		}
		if (!heroClass.isSidekick()) {
			icon = String.format("class-%s", heroClass.getEnglishName().replace(' ', '-').toLowerCase());
		}
		
		image = String.format("https://image.dnd5.club:8089/classes/background/class-%s.webp", heroClass.getEnglishName().replace(' ', '-').toLowerCase());
	}
}