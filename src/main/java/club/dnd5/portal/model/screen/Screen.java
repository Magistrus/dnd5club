package club.dnd5.portal.model.screen;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.races.Race;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "screens")
public class Screen {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String englishName;
	
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "parent_id")
	private Screen parent;

	@OneToMany(mappedBy = "parent", orphanRemoval = true)
	private List<Screen> chields;

	@Column(columnDefinition = "TEXT")
	private String description;
	@ManyToOne
	@JoinColumn(name = "source")
	private Book book;
}