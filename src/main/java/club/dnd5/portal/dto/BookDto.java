package club.dnd5.portal.dto;

import club.dnd5.portal.model.book.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
	private String id;
	private String name;
	private String englishName;
	private String type;
	public BookDto(Book book) {
		id = book.getSource();
		name = book.getName();
		englishName = book.getEnglishName();
		type = book.getType().getName();
	}
}