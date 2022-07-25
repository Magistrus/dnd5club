package club.dnd5.portal.dto.api.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.items.Weapon;
import club.dnd5.portal.model.items.WeaponProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class PropertyApi {
	private String name;
	private String url;
	private String twoHandDice;
	private String distance;
	private String description;
	public PropertyApi(Weapon weapon, WeaponProperty property) {
		name = property.getName();
		if ("Versatile".equals(property.getEnglishName())){
			twoHandDice = String.format("1к%d",weapon.getTwoHandDamageDice().getMaxValue());
		}
		if ("Thrown".equals(property.getEnglishName()) || "Ammunition".equals(property.getEnglishName())){
			distance = String.format("%d/%d",weapon.getMinDistance(), weapon.getMaxDistance());
		}
		description = property.getDescription();
		url = String.format("/screens/%s", property.getEnglishName());
	}
}
