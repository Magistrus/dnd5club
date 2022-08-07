package club.dnd5.portal.dto.api.bookmark;

import java.util.List;
import java.util.stream.Collectors;

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
	private String parentUuid;
	private List<BookmarkApi> children;

	public BookmarkApi(Bookmark bookmark) {
		uuid = bookmark.getUuid().toString();
		order = bookmark.getOrder();
		name = bookmark.getName();
		if (bookmark.getUrl() != null) {
			url = bookmark.getUrl();
			if (url.startsWith("/options")) {
				type = "option";
			} else if (url.startsWith("/traits")) {
				type = "trait";
			} else if (url.startsWith("/armors")) {
				type = "armor";
			} else if (url.startsWith("/weapons")) {
				type = "weapon";
			} else if (url.startsWith("/items/magic")) {
				type = "magic-item";
			} else if (url.startsWith("/items")) {
				type = "item";
			} else if (url.startsWith("/screens")) {
				type = "screen";
			} else if (url.startsWith("/bestiary")) {
				type = "creature";
			} else if (url.startsWith("/spells")) {
				type = "spell";
			} else if (url.startsWith("/gods")) {
				type = "god";
			}
		}
		if (bookmark.getParent() != null) {
			parentUuid = bookmark.getParent().getUuid().toString();
		}
		if (!bookmark.getChildren().isEmpty()) {
			children = bookmark.getChildren()
					.stream()
					.map(BookmarkApi::new)
					.collect(Collectors.toList());
		}
	}
}
