package club.dnd5.portal.dto.api.wiki;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.dto.api.classes.NameApi;
import club.dnd5.portal.model.screen.Screen;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class ScreenApi {
	protected NameApi name;
	protected String url;
	protected Integer order;
	protected SourceApi source;
	private String icon;
	private String group;

	public ScreenApi(Screen screen) {
		name = new NameApi(screen.getName(), screen.getEnglishName());
		url = String.format("/screens/%s", screen.getUrlName());
		order = screen.getOrdering();
		group = screen.getCategory();
		source = new SourceApi(screen.getBook());

		if (screen.getIcon() != null) {
			icon = screen.getIcon();
		}
	}
}
