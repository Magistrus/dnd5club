package club.dnd5.portal.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class SourceApiDto {
	private String shortName;
	private String name;
	private Boolean homebrew;
	private Short page;
	
	public SourceApiDto(Book book) {
		name = book.getName();
		shortName = book.getSource();
		if (book.getType() == TypeBook.CUSTOM) {
			homebrew = Boolean.TRUE;
		}
	}
	
	public SourceApiDto(Book book, Short page) {
		this(book);
		if (page != null) {
			this.page = page;
		}
	}
}