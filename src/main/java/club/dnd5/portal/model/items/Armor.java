package club.dnd5.portal.model.items;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import club.dnd5.portal.model.book.Book;
import lombok.Getter;

@Entity
@Table(name = "armors")
@Getter
public class Armor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String englishName;
	private String altName;
	private int AC;
	private int cost;
	private float weight;

	@Column(nullable = true)
	private Integer forceRequirements;
	private boolean stelsHindrance;

	@Enumerated(EnumType.ORDINAL)
	private ArmorCategory type;
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "source")
	private Book book;
}