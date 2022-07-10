package club.dnd5.portal.dto.api.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.dto.api.classes.NameApi;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.items.Equipment;
import club.dnd5.portal.model.items.MagicItem;
import club.dnd5.portal.model.items.Treasure;
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
	protected TypeApi type;
	private Object price;
	protected SourceApi source;
	
	public ItemApi(Equipment item) {
		name = new NameApi(item.getName(), item.getEnglishName());
		url = String.format("/items/%s", item.getEnglishName().replace(' ', '_'));
		if (item.getBook().getType() == TypeBook.CUSTOM) {
			homebrew = Boolean.TRUE;	
		}
	}

	public ItemApi(MagicItem item) {
		name = new NameApi(item.getCapitalazeName(), item.getEnglishName());
		url = String.format("/items/magic/%s", item.getEnglishName().replace(' ', '_'));
		if (item.getBook().getType() == TypeBook.CUSTOM) {
			homebrew = Boolean.TRUE;	
		}
	}

	public ItemApi(Treasure item) {
		name = new NameApi(item.getName(), item.getEnglishName());
		if (item.getBook().getType() == TypeBook.CUSTOM) {
			homebrew = Boolean.TRUE;	
		}
		type = new TypeApi(item.getType().getName(), item.getType().ordinal());
		if (item.getCost() != null && item.getCost() != 0) {
			price = item.getCost();
		}
		source = new SourceApi(item.getBook());
	}
	
	public void changeName(String newName) {
		name.setRus(String.format("%s %s", name.getRus(), newName));
	}
}