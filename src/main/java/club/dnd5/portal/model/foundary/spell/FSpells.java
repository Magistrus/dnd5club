package club.dnd5.portal.model.foundary.spell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.creature.Spellcater;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class FSpells {
	public FCellSpell spell1 = new FCellSpell();
	public FCellSpell spell2 = new FCellSpell();
	public FCellSpell spell3 = new FCellSpell();
	public FCellSpell spell4 = new FCellSpell();
	public FCellSpell spell5 = new FCellSpell();
	public FCellSpell spell6 = new FCellSpell();
	public FCellSpell spell7 = new FCellSpell();
	public FCellSpell spell8 = new FCellSpell();
	public FCellSpell spell9 = new FCellSpell();
	public FCellSpell pact = new FCellSpell();
	public FCellSpell spell0 = new FCellSpell();
	
	public FSpells(Creature creature) {
		for (Spellcater spellcater : creature.getSpellcasters()) {
			 spell1 = new FCellSpell(spellcater.getSlot1(), null, spellcater.getSlot1());
			 spell2 = new FCellSpell(spellcater.getSlot2(), null, spellcater.getSlot2());
			 spell3 = new FCellSpell(spellcater.getSlot3(), null, spellcater.getSlot3());
			 spell4 = new FCellSpell(spellcater.getSlot4(), null, spellcater.getSlot4());
			 spell5 = new FCellSpell(spellcater.getSlot5(), null, spellcater.getSlot5());
			 spell6 = new FCellSpell(spellcater.getSlot6(), null, spellcater.getSlot6());
			 spell7 = new FCellSpell(spellcater.getSlot7(), null, spellcater.getSlot7());
			 spell8 = new FCellSpell(spellcater.getSlot8(), null, spellcater.getSlot8());
			 spell9 = new FCellSpell(spellcater.getSlot9(), null, spellcater.getSlot9());
			 pact = new FCellSpell();
			 spell0 = new FCellSpell();
		}
	}
}