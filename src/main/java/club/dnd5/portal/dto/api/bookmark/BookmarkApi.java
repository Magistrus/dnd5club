package club.dnd5.portal.dto.api.bookmark;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class BookmarkApi {
	private String uuid;
	private String name; // пустой для главного набора закладок
	private String url; // пустой для категорий и имени набора закладок
	private String bodyType; // пустой для категорий и имени набора закладок

	private String parentUuid;
	private List<BookmarkApi> childs;
}