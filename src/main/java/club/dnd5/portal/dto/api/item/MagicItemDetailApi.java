package club.dnd5.portal.dto.api.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.dto.api.item.MagicItemApi;
import club.dnd5.portal.model.items.MagicItem;
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
	
	private SourceApi source;
	public MagicItemDetailApi(MagicItem item) {
		super(item);
		url = null;
		description = item.getDescription();
		if (item.getCustomization()) {
			custamization = item.getCustomization(); 
		}
		source = new SourceApi(item.getBook());
	}
}