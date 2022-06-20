package club.dnd5.portal.dto.api.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.model.items.Armor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class ArmorDetailApi extends ArmorApi {
	private SourceApi source;
	private Float weight;
	private String description;
	private Boolean disadvantage;
	private Integer requirement;
	private String duration;
	
	public ArmorDetailApi(Armor armor) {
		super(armor);
		url = null;
		source = new SourceApi(armor.getBook());
		weight = armor.getWeight();
		description = armor.getDescription();
		if (armor.isStelsHindrance()) {
			disadvantage = Boolean.TRUE;
		}
		if (armor.getForceRequirements() != null) {
			requirement = armor.getForceRequirements();
		}
		duration = String.format("%s/%s", armor.getType().getPutting(), armor.getType().getRemoval());
	}
}