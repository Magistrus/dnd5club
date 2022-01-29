package club.dnd5.portal.model.foundary;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import club.dnd5.portal.model.creature.Creature;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class FMovement {
	private Short burrow;
	private Short climb;
	private Short fly;
	 @JsonProperty("fly")
	private FFly fflay;
	private Short swim;
	private Byte walk = 0;
	//private String units = "ft";
	private Boolean canHover;
	
	public FMovement(Creature creature) {
		walk = creature.getSpeed();
		if (creature.getClimbingSpeed() != null) {
			climb = creature.getClimbingSpeed();
		}
		if (creature.getFlySpeed() != null) {
			if (creature.getHover()!= null && creature.getHover() == 1) {
				fflay = new FFly(creature.getFlySpeed());
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
		canHover = creature.getHover() != null && creature.getHover() == 1; 
	}
}