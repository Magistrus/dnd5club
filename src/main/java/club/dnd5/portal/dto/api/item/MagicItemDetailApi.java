package club.dnd5.portal.dto.api.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
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
	private Boolean custamization;
	private Collection<String> detailType;
	private Collection<String> detailCustamization;
	private CostApi cost;
	private SourceApi source;
	private Collection<String> images;
	
	public MagicItemDetailApi(MagicItem item) {
		super(item);
		url = null;
		description = item.getDescription();
		if (item.getCustomization()) {
			custamization = item.getCustomization(); 
		}
		if (!item.getArmors().isEmpty()) {
			detailType = item.getArmors().stream().map(Armor::getName).collect(Collectors.toList());
		}
		if (!item.getWeapons().isEmpty()) {
			detailType = item.getWeapons().stream().map(Weapon::getName).collect(Collectors.toList());
		}
		if (item.getSpecial() !=null) {
			if (detailType == null) {
				detailType = new ArrayList<>();
			}
			detailType.add(item.getSpecial());
		}
		source = new SourceApi(item.getBook());
		if (!item.getCustClasses().isEmpty()) {
			detailCustamization = item.getCustClasses().stream().map(HeroClass::getCapitalazeName).collect(Collectors.toList());
		}
		cost = new CostApi(item);
	}
}