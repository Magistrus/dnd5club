package club.dnd5.portal.dto.api.bookmark;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.BookmarkCategory;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class CategoryApi {
	private String name;
	private String code;
	private String itemType;
	private Integer order;
	
	public CategoryApi(BookmarkCategory category) {
		name = category.getName();
		code = category.getCode();
		if (category.getItemType() != null) {
			itemType = category.getItemType();
		}
		order = category.getOrder();
	}
}
