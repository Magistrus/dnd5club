package club.dnd5.portal.model.classes.archetype;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import club.dnd5.portal.model.splells.Spell;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "archetype_spell")
public class ArchetypeSpell {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String advenced;
	private int level;
	private String type;
	
	@OneToOne
	private Spell spell;
}