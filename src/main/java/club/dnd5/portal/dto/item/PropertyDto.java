package club.dnd5.portal.dto.item;

import club.dnd5.portal.model.items.WeaponProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PropertyDto {
	private String name;
	private String englishName;
	PropertyDto(WeaponProperty property){
		name = property.getName();
		englishName = property.getEnglishName();
	}
}