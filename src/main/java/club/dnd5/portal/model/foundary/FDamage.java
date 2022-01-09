package club.dnd5.portal.model.foundary;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.creature.Creature;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FDamage {
	private List<String> value;
	private String custom ="";
	public FDamage(Creature creature, Supplier<List<DamageType>> values) {
		value = values.get().stream()
				.map(DamageType::name)
				.map(String::toLowerCase)
				.collect(Collectors.toList());
	}
}