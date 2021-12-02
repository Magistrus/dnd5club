package club.dnd5.portal.dto;


import club.dnd5.portal.model.creature.Creature;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RandomCreatureDto {
	private int count;
	private Creature creature;
}