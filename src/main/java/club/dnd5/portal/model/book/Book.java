package club.dnd5.portal.model.book;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "books")
public class Book implements Serializable, Comparable<Book>{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(unique = true, nullable = false)
	private String source;
	private String name;
	private String englishName;

	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Enumerated(EnumType.STRING) 
	private TypeBook type;
	
	public Book(String source) {
		this.source = source;
	}

	@Override
	public int compareTo(Book b) {
		return type.compareTo(b.getType());
	}
	
	@Override
	public String toString() {
		return source;
	}
}