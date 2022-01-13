package club.dnd5.portal.model.foundary;


import club.dnd5.portal.model.creature.Creature;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FMovement {
	private short burrow;
	private short climb;
	private short fly;
	private short swim;
	private short walk;
	private String units = "ft";
	private boolean hover;

	public FMovement(Creature creature) {
		walk = creature.getSpeed();
		if (creature.getClimbingSpeed() != null) {
			climb = creature.getClimbingSpeed();
		}
		if (creature.getFlySpeed() != null) {
			fly = creature.getFlySpeed();
		}
		if (creature.getSwimmingSpped() != null) {
			swim = creature.getSwimmingSpped();
		}
		if (creature.getDiggingSpeed() != null) {
			burrow = creature.getDiggingSpeed();
		}
		hover = creature.getHover() != null && creature.getHover() == 1; 
	}
}