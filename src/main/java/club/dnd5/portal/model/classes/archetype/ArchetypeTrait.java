package club.dnd5.portal.model.classes.archetype;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.thymeleaf.util.StringUtils;

import club.dnd5.portal.model.splells.Spell;
import lombok.Getter;

@Getter
@Entity
@Table(name = "hero_class_feats")
public class ArchetypeTrait {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private byte level;
	@Column(columnDefinition = "TEXT")
	private String description;
	private String child;
	
	@ManyToOne
	private Archetype archetype;
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<Spell> spells;
	
	public String getCapitalizeName() {
		return StringUtils.capitalizeWords(name.toLowerCase());
	}
}