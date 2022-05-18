package club.dnd5.portal.model.foundary;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.creature.Creature;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class FSpeed {
	private Byte walk = 0;
	private Object fly;
	private Short burrow;
	private Short climb;
	private Short swim;
	//private String units = "ft";
	private Boolean canHover;
	
	public FSpeed(Creature creature) {
		walk = creature.getSpeed();
		if (creature.getClimbingSpeed() != null) {
			climb = creature.getClimbingSpeed();
		}
		if (creature.getFlySpeed() != null) {
			if (creature.getHover()!= null && creature.getHover() == 1) {
				fly = new FFly(creature.getFlySpeed());
			} else {
				fly = creature.getFlySpeed();
			}
		}
		if (creature.getSwimmingSpped() != null) {
			swim = creature.getSwimmingSpped();
		}
		if (creature.getDiggingSpeed() != null) {
			burrow = creature.getDiggingSpeed();
		}
		if (creature.getHover() != null && creature.getHover()==1) {
			canHover = Boolean.TRUE;	
		}
	}
}