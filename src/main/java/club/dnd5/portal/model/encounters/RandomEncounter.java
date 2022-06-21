package club.dnd5.portal.model.encounters;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.creature.HabitatType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity()
@Table(name = "encounters")
public class RandomEncounter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer level;
	private String formula;
	private String environment;
	
	@Enumerated (EnumType.STRING)
	private HabitatType type;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "source")
	private Book book;
}