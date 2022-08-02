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
	private String name; // пустой для главного набора закладок
	private String url; // пустой для категорий и имени набора закладок
	private String bodyType; // пустой для категорий и имени набора закладок
	private Integer order;

	private String parentUuid;
	private List<BookmarkApi> childs;
	
	public BookmarkApi(Bookmark bookmark) {
		uuid = bookmark.getUuid().toString();
		if (bookmark.getName() != null) {
			name = bookmark.getName();
		}
		if (bookmark.getUrl() != null) {
			url = bookmark.getUrl();
		}
		if (url.startsWith("/options")) {
			bodyType = "option";
		} else if (url.startsWith("/traits")) {
			bodyType = "trait";
		} else if (url.startsWith("/armors")) {
			bodyType = "armor";
		} else if (url.startsWith("/weapons")) {
			bodyType = "weapon";
		} else if (url.startsWith("/items/magic")) {
			bodyType = "item-magic";
		} else if (url.startsWith("/items")) {
			bodyType = "item";
		} else if (url.startsWith("/screens")) {
			bodyType = "screen";
		} else if (url.startsWith("/bestiary")) {
			bodyType = "creature";
		} else if (url.startsWith("/spells")) {
			bodyType = "spell";
		} else if (url.startsWith("/gods")) {
			bodyType = "god";
		}
		if (bookmark.getParent() != null) {
			parentUuid = bookmark.getParent().getUuid().toString();
		}
		order = bookmark.getOrder();
		if (bookmark.getChields().isEmpty()) {
			childs = bookmark.getChields()
					.stream()
					.map(BookmarkApi::new)
					.collect(Collectors.toList());
		}
	}
}