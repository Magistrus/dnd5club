package club.dnd5.portal.dto.api.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.items.Armor;
import club.dnd5.portal.model.items.MagicItem;
import club.dnd5.portal.model.items.Weapon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class MagicItemDetailApi extends MagicItemApi {
	private String description;
	private Boolean customization;
	private Collection<String> detailType;
	private Collection<String> detailCustamization;
	private CostApi cost;
	private Collection<String> images;
	
	public MagicItemDetailApi(MagicItem item) {
		super(item);
		url = null;
		description = item.getDescription();
		if (item.getCustomization()) {
			customization = item.getCustomization(); 
		}
		if (!item.getArmors().isEmpty()) {
			detailType = item.getArmors().stream().map(Armor::getName).map(String::toLowerCase).collect(Collectors.toList());
		}
		if (!item.getWeapons().isEmpty()) {
			detailType = item.getWeapons().stream().map(Weapon::getName).map(String::toLowerCase).collect(Collectors.toList());
		}
		if (item.getSpecial() !=null) {
			if (detailType == null) {
				detailType = new ArrayList<>();
			}
			detailType.add(item.getSpecial());
		}

		if (!item.getCustClasses().isEmpty()) {
			detailCustamization = item.getCustClasses().stream().map(HeroClass::getAblativeName).collect(Collectors.toList());
		}
		if (item.getCustSpecial() != null) {
			if (detailCustamization == null) {
				detailCustamization = new ArrayList<>(1);
			}
			detailCustamization.add(item.getCustSpecial()); 
		}
		cost = new CostApi(item);
	}
}