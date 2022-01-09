package club.dnd5.portal.model.foundary;

import club.dnd5.portal.model.creature.Creature;

public class FSenses {
	public int darkvision;
	public int blindsight;
	public int tremorsense;
	public int truesight;
	public String units;
	public String special;

	public FSenses(Creature creature) {
		darkvision = creature.getDarkvision() == null ? 0 : creature.getDarkvision();
		blindsight = creature.getBlindsight() == null ? 0 : creature.getBlindsight();
		tremorsense = creature.getVibration() == null ? 0 : creature.getVibration();
		truesight = creature.getTrysight() == null ? 0 : creature.getTrysight();
		units = "ft";
		special = "";
	}
}