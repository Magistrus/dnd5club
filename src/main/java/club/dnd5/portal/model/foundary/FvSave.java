package club.dnd5.portal.model.foundary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.creature.SavingThrow;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Getter
@NoArgsConstructor
public class FvSave {
    public String con;
    @JsonProperty("int") 
    public String myint;
    public String wis;
    public String dex;
    public String cha;
    public String str;
    public FvSave(Creature creature) {
    	for (SavingThrow savingThrow : creature.getSavingThrows()) {
			switch (savingThrow.getAbility()) {
			case STRENGTH:
				str = String.format("%+d", savingThrow.getBonus());
				break;
			case DEXTERITY:
				dex = String.format("%+d", savingThrow.getBonus());
				break;
			case CONSTITUTION:
				con = String.format("%+d", savingThrow.getBonus());
				break;
			case INTELLIGENCE:
				myint = String.format("%+d", savingThrow.getBonus());
				break;
			case WISDOM:
				wis = String.format("%+d", savingThrow.getBonus());
				break;
			case CHARISMA:
				cha = String.format("%+d", savingThrow.getBonus());
				break;
			default:
				break;
			}
		}
    }
}