package club.dnd5.portal.dto.api;

import club.dnd5.portal.model.book.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SourceApiDto {
	private String shortName;
	private String name;
	public SourceApiDto(Book book) {
		shortName = book.getSource();
		name = book.getName();
	}
}