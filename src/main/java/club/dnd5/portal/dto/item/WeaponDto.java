package club.dnd5.portal.dto.item;

import java.util.List;
import java.util.stream.Collectors;

import club.dnd5.portal.model.items.Weapon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WeaponDto {
	private int id;
	private String name;
	private String englishName;
	private String cost;
	private String damage;
	private String damageType;
	private String versatile;
	private String distance;
	private String weight;
	private String type;
	private List<PropertyDto> properties;
	private String book;
	private String bookshort;
	
	public WeaponDto(Weapon weapon) {
		id = weapon.getId();
		name = weapon.getName();
		englishName = weapon.getEnglishName();
		cost = weapon.getCost() + " " + weapon.getCurrency().getName();
		weight = String.valueOf(weapon.getWeight());
		damage = weapon.getNumberDice() == null ? "0" : weapon.getNumberDice() + (weapon.getDamageDice() == null ? "" : weapon.getDamageDice().getName());
		damageType = weapon.getDamageType().getCyrilicName();
		if (weapon.getTwoHandDamageDice() != null) {
			versatile = "1к" + weapon.getTwoHandDamageDice().getMaxValue();
		}
		distance = " (дис. " + weapon.getMinDistance() + "/" + weapon.getMaxDistance()+")";
		type = weapon.getType().getName();
		properties = weapon.getProperties().stream().map(PropertyDto::new).collect(Collectors.toList());
		book = weapon.getBook().getName();
		bookshort = weapon.getBook().getSource(); 
	}
}