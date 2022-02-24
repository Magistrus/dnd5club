package club.dnd5.portal.model.races;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import club.dnd5.portal.model.Language;
import club.dnd5.portal.model.SkillType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "race_features")
public class Feature {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String englishName;
	@Column(columnDefinition = "TEXT")
	private String description;
	private boolean feature;
	
	@ManyToMany
	private List<Language> lanuages;
	
	@ElementCollection(targetClass = SkillType.class)
	@CollectionTable(name = "race_feature_skill_type")
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private List<SkillType> skills;

	private Integer replaceFeatureId;
	
	public boolean isNotFeature() {
		return !feature;
	}
}