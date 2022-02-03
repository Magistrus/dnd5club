package club.dnd5.portal.model.foundary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.creature.Skill;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)

@Getter
@NoArgsConstructor
public class FvSkill {
	public String perception;
	public String history;
	public String stealth;
	public String medicine;
	public String religion;
	public String persuasion;
	public String insight;
	public String deception;
	public String arcana;
	public String athletics;
	public String acrobatics;

	public FvSkill(Creature creature) {
		for(Skill skill : creature.getSkills()) {
			switch (skill.getType()) {
			case PERCEPTION:
				perception = String.format("%+d", skill.getBonus());
				break;
			case HISTORY:
				history = String.format("%+d", skill.getBonus());
				break;
			case STEALTH:
				stealth = String.format("%+d", skill.getBonus());
				break;
			case MEDICINE:
				medicine = String.format("%+d", skill.getBonus());
				break;
			case RELIGION:
				religion = String.format("%+d", skill.getBonus());
				break;
			case PERSUASION:
				persuasion = String.format("%+d", skill.getBonus());
				break;
			case INSIGHT:
				insight = String.format("%+d", skill.getBonus());
				break;
			case ARCANA:
				arcana = String.format("%+d", skill.getBonus());
				break;
			case DECEPTION:
				deception = String.format("%+d", skill.getBonus());
				break;
			case ATHLETICS:
				athletics = String.format("%+d", skill.getBonus());
				break;
			case ACROBATICS:
				acrobatics = String.format("%+d", skill.getBonus());
				break;
			default:
				break;
			}
		}
	}
}