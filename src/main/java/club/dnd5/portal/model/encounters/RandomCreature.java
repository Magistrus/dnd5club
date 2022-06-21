package club.dnd5.portal.model.encounters;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import club.dnd5.portal.model.Dice;
import club.dnd5.portal.model.creature.Creature;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "random_creatures")
public class RandomCreature {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer countDice;
	@Enumerated(EnumType.STRING)
	private Dice dice;
	private int count;
	@OneToOne
	@JoinColumn(name = "creature_id")
	private Creature creature;
}