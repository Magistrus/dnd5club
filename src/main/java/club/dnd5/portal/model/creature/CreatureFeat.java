package club.dnd5.portal.model.creature;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "creature_feats")
@Data
public class CreatureFeat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	//private String englishName;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	private String img;
}