package club.dnd5.portal.dto.api.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.classes.NameApi;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.items.Armor;
import club.dnd5.portal.model.items.Currency;
import club.dnd5.portal.model.items.Equipment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class ItemApi {
	private NameApi name;
	protected String url;
	private Boolean homebrew;
	private TypeApi type;
	private String armorClass;
	private String price;
	
	public ItemApi(Equipment item) {
		name = new NameApi(item.getName(), item.getEnglishName());
		url = String.format("/armors/%s", item.getEnglishName().replace(' ', '_'));
		if (item.getBook().getType() == TypeBook.CUSTOM) {
			homebrew = Boolean.TRUE;	
		}
	}
}