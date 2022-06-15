package club.dnd5.portal.dto.api.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.items.MagicItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class CostApi {
	private String dmg;
	private String xge;
	public CostApi(MagicItem item) {
		dmg = item.getRangeCostDMG();
		xge = item.getRangeCostXGE();
	}
}