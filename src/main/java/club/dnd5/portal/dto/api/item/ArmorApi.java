package club.dnd5.portal.dto.api.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.classes.NameApi;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.items.Armor;
import club.dnd5.portal.model.items.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class ArmorApi {
	private NameApi name;
	protected String url;
	private Boolean homebrew;
	private TypeApi type;
	private String armorClass;
	private String price;
	
	public ArmorApi(Armor armor) {
		name = new NameApi(armor.getName(), armor.getEnglishName());
		url = String.format("/armors/%s", armor.getEnglishName().replace(' ', '_'));
		if (armor.getBook().getType() == TypeBook.CUSTOM) {
			homebrew = Boolean.TRUE;	
		}
		type = new TypeApi(armor.getType().getName(), armor.getType().ordinal());
		switch (armor.getType()) {
			case LIGHT:
				armorClass = String.format("%d + модификатор Лов", armor.getAC());
				break;
			case MEDIUM:
				armorClass = String.format("%d + модификатор Лов (макс. 2)", armor.getAC());
				break;
			case SHIELD:
				armorClass = String.format("+%d", armor.getAC());
				break;
			default:
				armorClass = String.format("%d", armor.getAC());
		}
		price = String.format("%d %s.", armor.getCost(), Currency.GM.getName());
	}
}