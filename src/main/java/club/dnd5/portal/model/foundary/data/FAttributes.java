package club.dnd5.portal.model.foundary.data;

import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.ArmorType;
import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.creature.Spellcater;
import club.dnd5.portal.model.foundary.FAC;
import club.dnd5.portal.model.foundary.FHP;
import club.dnd5.portal.model.foundary.FInit;
import club.dnd5.portal.model.foundary.FSpeed;
import club.dnd5.portal.model.foundary.FSenses;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FAttributes {
	private FAC ac;
	private FHP hp;
	private FInit init;
	private FSpeed speed;
	public FSenses senses;
	public String spellcasting = "";
	private byte prof;
	private byte spelldc = 10;
	private byte spellLevel;

	public FAttributes(Creature creature) {
		if (creature.getArmorTypes().contains(ArmorType.NATURAL)) {
			ac = new FAC(creature.getAC(), "natural", "", (byte) (10 + AbilityType.getModifier(creature.getDexterity())));
		} else {
			ac = new FAC(creature.getAC(), "default", "", (byte) (10 + AbilityType.getModifier(creature.getDexterity())));
		}
		hp = new FHP(creature);
		init = new FInit();
		speed = new FSpeed(creature);
		senses = new FSenses(creature);
		if (!creature.getSpellcasters().isEmpty()) {
			for (Spellcater spellcaster : creature.getSpellcasters()) {
				if (spellcaster.getLevel() > 0) {
					spellcasting = spellcaster.getSpellAbility().name().toLowerCase().substring(0, 3);
					spelldc = spellcaster.getDc();
				}
			}
		}
	}
}