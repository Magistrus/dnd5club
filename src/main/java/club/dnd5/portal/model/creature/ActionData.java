package club.dnd5.portal.model.creature;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.items.Weapon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "creature_actions_data")
public class ActionData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private byte attackBonus;
	private byte limitUse;
	
	@Enumerated(EnumType.STRING)
	private ActionDataType type;
	
	@OneToMany
	@JoinColumn(name = "action_data_id")
	private List<Damage> damages;
	
	private AbilityType savingThrowType;
	private Byte dc;
	
	@OneToOne
	private Weapon weapon;
}