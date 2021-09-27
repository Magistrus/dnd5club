package club.dnd5.portal.dto.bestiary;

import java.util.stream.Collectors;

import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.creature.HabitatType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatureDto {
	private int id; 
	private String name;
	private String englishName;
	private String altName;
	private String type;
	private String size;
	private String alignment;
	private int exp;
	private String cr;
	private String habitates;
	private String book;

	public CreatureDto(Creature creature) {
		id = creature.getId();
		exp = creature.getExp();
		cr = creature.getChallengeRating();
		name = creature.getName();
		altName = creature.getAltName();
		englishName = creature.getEnglishName();
		size = creature.getSize().getSizeName(creature.getType());
		type = creature.getType().getCyrilicName();
		alignment = creature.getAlignment().getCyrilicName();
		habitates = creature.getHabitates().stream().map(HabitatType::getName).collect(Collectors.joining(", "));
		book = creature.getBook().getName() + (creature.getPage() != null ? ", стр. " + creature.getPage() : "");
	}
}