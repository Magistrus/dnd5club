package club.dnd5.portal.model.foundary.data;

import java.util.List;

import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.foundary.FActivation;
import club.dnd5.portal.model.foundary.FArmor;
import club.dnd5.portal.model.foundary.FConsume;
import club.dnd5.portal.model.foundary.FDamage;
import club.dnd5.portal.model.foundary.FDuration;
import club.dnd5.portal.model.foundary.FHP;
import club.dnd5.portal.model.foundary.FRange;
import club.dnd5.portal.model.foundary.FSave;
import club.dnd5.portal.model.foundary.FTarget;
import club.dnd5.portal.model.foundary.FUses;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FData {
    public FDescription description;
    public String source;
    public int quantity;
    public int weight;
    public int price;
    public int attunement;
    public boolean equipped;
    public String rarity;
    public boolean identified;
    public FActivation activation;
    public FDuration duration;
    public FTarget target;
    public FRange range;
    public FUses uses;
    public FConsume consume;
    public String ability;
    public String actionType;
    public Object attackBonus;
    public String chatFlavor;
    public Object critical;
    public FDamage damage;
    public String formula;
    public FSave save;
    public FArmor armor;
    public FHP hp;
    public String weaponType;
    public FProperties properties;
    public boolean proficient;
    public List<String> cptooltipmode;
    public String requirements;
    public FRecharge recharge;
    public int level;
    public String school;
    public FComponents components;
    public FMaterials materials;
    public FPreparation preparation;
    public FScaling scaling;
    public FSpellType spellType;
	
	public FData(Creature creature) {
		description = new FDescription();
	}	
}