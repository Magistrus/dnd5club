package club.dnd5.portal.dto.api.item;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApiDto;
import club.dnd5.portal.model.items.Weapon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class WeaponDetailApi extends WeaponApi {
	private SourceApiDto source;
	private Float weight;
	private String description;
	private String special;
	private List<PropertyApi> properties;
	public WeaponDetailApi(Weapon weapon) {
		super(weapon);
		url = null;
		source = new SourceApiDto(weapon.getBook());
		weight = weapon.getWeight();
		description = weapon.getDescription();
		special = weapon.getSpecial();
		properties = weapon.getProperties()
				.stream()
				.map(property-> new PropertyApi(weapon, property))
				.collect(Collectors.toList());
	}
}