package club.dnd5.portal.model.encounters;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "random_encounter_tables")
public class RandomEncounterеTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition = "TEXT")
	private String name;
	
	private Integer level;
	private String formula;

	@Enumerated (EnumType.STRING)
	private HabitatType type;
	private String environment;
	
	@OneToMany
	@JoinColumn(name = "encounter_id")
	private List<RandomEncounterRow> encounters;
	
	@ManyToOne
	@JoinColumn(name = "source")
	private Book book;
}