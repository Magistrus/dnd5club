package club.dnd5.portal.model.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import club.dnd5.portal.model.book.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name =  "user_sources")
public class UserSource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "source")
	private Book book;
	private Boolean enabled;
}