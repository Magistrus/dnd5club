package club.dnd5.portal.dto.api.classes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.trait.Trait;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class TraitApi {
	protected NameApi name;
	private String url;
	private String requirements ;
	private Boolean homebrew;
	public TraitApi(Trait trait) {
		name = new NameApi(trait.getName(), trait.getEnglishName());
		url = String.format("/traits/%s", trait.getUrlName());
		if (trait.getRequirement()!=null) {
			requirements  = trait.getRequirement();
		}
		if (trait.getBook().getType() == TypeBook.CUSTOM) {
			homebrew = Boolean.TRUE;
		}
	}
}