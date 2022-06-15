package club.dnd5.portal.dto.api.item;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.model.items.Equipment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class ItemDetailApi extends ItemApi {
	private SourceApi source;
	private Float weight;
	private String description;
	private List<String> categories;
	private String image;
	
	public ItemDetailApi(Equipment item) {
		super(item);
		url = null;
		source = new SourceApi(item.getBook());
		weight = item.getWeight();
		if (item.getDescription() != null) {
			description = item.getDescription();
		}
		categories = item.getTypes().stream().map(category -> category.getCyrilicName()).collect(Collectors.toList());
		price = item.getTextCost();
	}
}