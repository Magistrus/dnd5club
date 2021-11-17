package club.dnd5.portal.model.splells;

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
@Table(name = "spell_source")
public class SpellSource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "source")
	private Book book;
	private int spellId;
	private int classId;
}