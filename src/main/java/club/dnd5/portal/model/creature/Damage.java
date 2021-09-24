package club.dnd5.portal.model.creature;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import club.dnd5.portal.model.DamageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name= "action_damage")
public class Damage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String formula;
	private String context;

	@Enumerated(EnumType.STRING)
	private DamageType type;
}