package club.dnd5.portal.model.foundary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.foundary.data.FAbilities;
import club.dnd5.portal.model.foundary.data.FAttributes;
import club.dnd5.portal.model.foundary.data.FBonuses;
import club.dnd5.portal.model.foundary.data.FCurrency;
import club.dnd5.portal.model.foundary.data.FResources;
import club.dnd5.portal.model.foundary.data.details.FDetails;
import club.dnd5.portal.model.foundary.spell.FSpells;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class FCreatureData {
    public FAbilities abilities;
    public FAttributes attributes;
    public FDetails details;
    public FTraits traits;
    public FCurrency currency;
    public FSkills skills;
    public FSpells spells;
    public FBonuses bonuses;
    public FResources resources;

	public FCreatureData(Creature creature) {
		abilities = new FAbilities(creature);
		attributes = new FAttributes(creature);
		details = new FDetails(creature);
		traits = new FTraits(creature);
		skills = new  FSkills(creature.getSkills());
		if (!creature.getSpellcasters().isEmpty())
		{
			spells = new FSpells(creature);
		} else {
			spells = new FSpells();
		}
		bonuses = new FBonuses();
		resources = new FResources(creature);
	}
}