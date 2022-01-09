package club.dnd5.portal.model.foundary;

import java.util.List;
import java.util.stream.Collectors;

import club.dnd5.portal.model.creature.Condition;
import club.dnd5.portal.model.creature.Creature;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FCondition {
	private List<String> value;
	private String custom ="";
	public FCondition(Creature creature) {
		value = creature.getImmunityStates().stream()
				.map(Condition::name)
				.map(String::toLowerCase)
				.collect(Collectors.toList());
	}
}