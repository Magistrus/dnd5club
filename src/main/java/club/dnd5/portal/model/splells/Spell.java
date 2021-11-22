package club.dnd5.portal.model.splells;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.classes.HeroClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "spells")
public class Spell {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Byte level;
	private Boolean ritual;
	
	private String name;
	@Column(unique = true)
	private String englishName;
	
	@Enumerated(EnumType.ORDINAL)
	private MagicSchool school;
	
	private int timeCast;
	
	@OneToMany
	@JoinColumn(name = "spell_id")
	private List<TimeCast> times;
	
	private String distance;
	
	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(columnDefinition = "TEXT", nullable = true)
	private String upperLevel;
	
	private Boolean verbalComponent;
	private Boolean somaticComponent;
	private Boolean materialComponent;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean consumable;
	
	@Column(columnDefinition = "TEXT", nullable = true)
	private String additionalMaterialComponent;
	private String duration;
	private Boolean concentration;
	
	@ElementCollection(targetClass = DamageType.class)
	@JoinTable(name = "spell_damage_type", joinColumns = @JoinColumn(name = "spell_id"))
	@Column(name = "damage_type", nullable = false)
	@Enumerated(javax.persistence.EnumType.STRING)
	private List<DamageType> damageType;
	
	@ManyToMany
	private List<HeroClass> heroClass;
	
	@ManyToOne
	@JoinColumn(name = "source")
	private Book book;
	private Short page;
	
	public String getFullName() {
		return name.toLowerCase() + " [" + englishName.toLowerCase() +"]";
	}
}