package club.dnd5.portal.model.fvtt.plutonium;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.ArmorType;
import club.dnd5.portal.model.creature.Creature;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Getter
@NoArgsConstructor
public class FvAC {
	private byte ac;
	private List<String> from;
	private String condition;
	private Boolean braces;

	public FvAC(Creature creature) {
		ac = creature.getAC();
		from = creature.getArmorTypes().stream().map(ArmorType::getPlutoniumTypeName).collect(Collectors.toList());
	}
}