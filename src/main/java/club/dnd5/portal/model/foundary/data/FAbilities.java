package club.dnd5.portal.model.foundary.data;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.foundary.FAbility;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({ "str", "dex", "con", "int", "wis", "cha" })
public class FAbilities {
	private FAbility str;
	private FAbility dex;
	private FAbility con;
    @JsonProperty("int")
	private FAbility intel;
	private FAbility wis;
	private FAbility cha;
	public FAbilities(Creature creature) {
		FAbility str = new FAbility();
		str.setValue(creature.getStrength());
		str.setProficient(isProficient(creature, AbilityType.STRENGTH));
		str.setMod(AbilityType.getModifier(str.getValue()));
		this.str = str;
		
		FAbility dex = new FAbility();
		dex.setValue(creature.getDexterity());
		dex.setProficient(isProficient(creature, AbilityType.DEXTERITY));
		dex.setMod(AbilityType.getModifier(dex.getValue()));
		this.dex = dex;
		
		FAbility con = new FAbility();
		con.setValue(creature.getConstitution());
		con.setProficient(isProficient(creature, AbilityType.CONSTITUTION));
		con.setMod(AbilityType.getModifier(con.getValue()));
		this.con = con;
		
		FAbility intel = new FAbility();
		intel.setValue(creature.getIntellect());
		intel.setProficient(isProficient(creature, AbilityType.INTELLIGENCE));
		intel.setMod(AbilityType.getModifier(intel.getValue()));
		this.intel = intel;
		
		FAbility wis = new FAbility();
		wis.setValue(creature.getWizdom());
		wis.setProficient(isProficient(creature, AbilityType.WISDOM));
		wis.setMod(AbilityType.getModifier(wis.getValue()));
		this.wis = wis;
		
		FAbility cha = new FAbility();
		cha.setValue(creature.getCharisma());
		cha.setProficient(isProficient(creature, AbilityType.CHARISMA));
		cha.setMod(AbilityType.getModifier(cha.getValue()));
		this.cha = cha;
	}
	
	private byte isProficient(Creature creature, AbilityType type) {
		return (byte) (creature.getSavingThrows().stream()
				.anyMatch(st -> st.getAbility().equals(type)) ? 1 : 0);
	}
}