package club.dnd5.portal.dto.api.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.classes.NameApi;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.items.Weapon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class WeaponApi {
	private NameApi name;
	protected String url;
	private Boolean homebrew;
	private TypeApi type;
	private DamageApi damage;
	private String price;
	
	public WeaponApi(Weapon weapon) {
		name = new NameApi(weapon.getName(), weapon.getEnglishName());
		url = String.format("/weapons/%s", weapon.getEnglishName().replace(' ', '_'));
		if (weapon.getBook().getType() == TypeBook.CUSTOM) {
			homebrew = Boolean.TRUE;	
		}
		type = new TypeApi(weapon.getType().getName(), weapon.getType().ordinal());
		damage = new DamageApi(weapon.getDamage(), weapon.getDamageType().getCyrilicName());
		price = String.format("%d %s.",weapon.getCost(), weapon.getCurrency().getName());
	}
}