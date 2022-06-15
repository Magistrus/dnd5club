package club.dnd5.portal.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.classes.NameApi;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.god.God;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class GodApi {
	protected NameApi name;
	protected String url;
	private Boolean homebrew;
	protected String alignment;
	
	public GodApi(God god) {
		name = new NameApi(god.getName(), god.getEnglishName());
		url = String.format("/gods/%s", god.getUrlName());
		if (god.getBook().getType() == TypeBook.CUSTOM) {
			homebrew = Boolean.TRUE;
		}
		alignment = god.getAligment().getShortName();
	}
}