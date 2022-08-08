package club.dnd5.portal.dto.api.bookmark;

import java.util.List;
import java.util.stream.Collectors;

import club.dnd5.portal.model.BookmarkSection;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
	private String parentUuid; // пустой для группы
	private List<BookmarkApi> children;

	public BookmarkApi(Bookmark bookmark) {
		uuid = bookmark.getUuid().toString();
		order = bookmark.getOrder();
		name = bookmark.getName();
		if (bookmark.getUrl() != null) {
			url = bookmark.getUrl();
			type = BookmarkSection.getSectionByURL(bookmark.getUrl()).getItemType();
		}
		if (bookmark.getParent() != null) {
			parentUuid = bookmark.getParent().getUuid().toString();
		}
	}
}
