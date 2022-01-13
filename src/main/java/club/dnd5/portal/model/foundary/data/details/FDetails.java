package club.dnd5.portal.model.foundary.data.details;

import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.creature.Spellcater;
import club.dnd5.portal.model.foundary.FValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FDetails {
    public FBiography biography;
    public String alignment;
    public String race;
    public FType type;
    public String environment;
    public double cr;
    public int spellLevel;
    public FValue xp;
    public String source;
    @JsonProperty("class")
    public FClass fclass;
    public String gender;
    public String age;
    public String height;
    public String weight;
    public String eyes;
    public String skin;
    public String hair;
    public String notes1name;
    public String notes2name;
    public String notes3name;
    public String notes4name;

	public FDetails(Creature creature) {
		biography = new FBiography(creature.getDescription(), "");
		alignment = creature.getAlignment().getCyrilicName();
		race = creature.getRaceName();
		type = new FType(creature.getType().name().toLowerCase(), "", "", "");
		environment = creature.getHabitates().stream().map(HabitatType::getName).collect(Collectors.joining(", "));
		switch (creature.getChallengeRating()) {
		case "1/2":
			cr = 1f/2;
			break;
		case "1/4":
			cr = 1f/4;
			break;
		case "1/8":
			cr = 1f/8;
			break;
		default:
			cr = Integer.valueOf(creature.getChallengeRating());
		}
		xp = new FValue(creature.getExp());
		if (!creature.getSpellcasters().isEmpty()) {
			for (Spellcater spellcaster : creature.getSpellcasters()) {
				spellLevel = spellcaster.getLevel();
			}
		}
		source = creature.getBook().getSource();
	}
}