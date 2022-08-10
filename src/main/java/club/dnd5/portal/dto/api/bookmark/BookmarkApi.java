package club.dnd5.portal.dto.api.bookmark;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.BookmarkCategory;
import club.dnd5.portal.model.user.Bookmark;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class BookmarkApi {
	private String uuid;
	private String name;
	private String url; // пустой для категорий и групп
	private String type; // пустой для категорий и групп
	private Integer order;
	private String parentUUID; // пустой для группы

	public BookmarkApi(Bookmark bookmark) {
		uuid = bookmark.getUuid().toString();
		order = bookmark.getOrder();
		name = bookmark.getName();
		if (bookmark.getUrl() != null) {
			url = bookmark.getUrl();
			type = BookmarkCategory.getCategoryByURL(bookmark.getUrl()).getItemType();
		}
		if (bookmark.getParent() != null) {
			parentUUID = bookmark.getParent().getUuid().toString();
		}
	}
}
