package club.dnd5.portal.dto.api.classes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.background.Background;
import club.dnd5.portal.model.book.TypeBook;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class BackgroundApi {
	protected NameApi name;
	private String url;
	private Boolean homebrew;
	public BackgroundApi(Background background) {
		name = new NameApi(background.getName(), background.getEnglishName());
		url = String.format("/backgrounds/%s", background.getUrlName());
		if (background.getBook().getType() == TypeBook.CUSTOM) {
			homebrew = Boolean.TRUE;
		}
	}
}