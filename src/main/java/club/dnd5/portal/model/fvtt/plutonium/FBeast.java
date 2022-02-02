package club.dnd5.portal.model.fvtt.plutonium;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.creature.Action;
import club.dnd5.portal.model.creature.ActionType;
import club.dnd5.portal.model.creature.Condition;
import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.foundary.FAltArt;
import club.dnd5.portal.model.foundary.FLegendary;
import club.dnd5.portal.model.foundary.FLegendaryGroup;
import club.dnd5.portal.model.foundary.FSoundClip;
import club.dnd5.portal.model.foundary.FSpeed;
import club.dnd5.portal.model.foundary.FSpellcasting;
import club.dnd5.portal.model.foundary.FVariant;
import club.dnd5.portal.model.foundary.FVersion;
import club.dnd5.portal.model.foundary.FvSave;
import club.dnd5.portal.model.foundary.FvSkill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class FBeast {
	public String name;
	public String source;
	public int page;
	public List<FOtherSource> otherSources;
	public String size; //
	public Object type; //
	public List<String> alignment; //
	public List<Object> ac = new ArrayList<>(2); //
	public FvHp hp;
	public FSpeed speed;
	public byte str;
	public byte dex;
	public byte con;
	@JsonProperty("int")
	public byte myint;
	public byte wis;
	public byte cha;
	public FvSkill skill;
	public int passive;
	public List<String> languages;
	public String cr;
	public List<FTrait> trait;
	public List<FAction> action;
	public List<FAction> reaction;
	public List<FLegendary> legendary;

	public FLegendaryGroup legendaryGroup;
	public List<String> environment;
	public FSoundClip soundClip;
	public List<String> languageTags = new ArrayList<>();
	public Set<String> damageTags = new TreeSet<>();
	public List<String> miscTags = new ArrayList<>();
	public boolean hasToken = true;
	public boolean hasFluff = true;
	public boolean hasFluffImages = true;
	public boolean srd;
	public FvSave save;
	public List<String> senses;

	public List<String> traitTags = new ArrayList<>();
	public List<String> senseTags = new ArrayList<>();
	public List<String> actionTags = new ArrayList<>();
	public List<String> conditionInflict;
	public List<String> conditionInflictLegendary;

	public boolean basicRules = true;
	public List<FSpellcasting> spellcasting;
	public List<String> spellcastingTags;
	public List<String> group;
	public List<FVariant> variant;
	public String dragonCastingColor;
	
	public List<Object> resist;
	public List<Object> immune;
	public List<String> conditionImmune;
	public List<String> vulnerable;
	
	public List<String> conditionInflictSpell;
	public List<FVersion> _versions;

	public List<FAltArt> altArt;

	public boolean familiar;

	public FBeast(Creature creature) {
		name = String.format("%s (%s)", creature.getName(), creature.getEnglishName());
		source = creature.getBook().getSource();
		size = String.valueOf(creature.getSize().name().charAt(0));
		type = creature.getType().name().toLowerCase();
		alignment = new ArrayList<>(2);
		switch (creature.getAligmentRaw()) {
		case CHAOTIC_EVIL:
			alignment.add("C");
			alignment.add("E");
			break;
		case CHAOTIC_NEUTRAL:
			alignment.add("C");
			alignment.add("N");
			break;
		case CHAOTIC_GOOD:
			alignment.add("C");
			alignment.add("G");
			break;
		case LAWFUL_EVIL:
			alignment.add("L");
			alignment.add("E");
			break;
		case LAWFUL_GOOD:
			alignment.add("L");
			alignment.add("G");
			break;
		case LAWFUL_NEUTRAL:
			alignment.add("L");
			alignment.add("N");
			break;
		case NEUTRAL:
			alignment.add("N");
			break;
		case NEUTRAL_EVIL:
			alignment.add("N");
			alignment.add("E");
			break;
		case NEUTRAL_GOOD:
			alignment.add("N");
			alignment.add("G");
			break;
		case TRUE_NEUTRAL:
			alignment.add("N");
			break;
		case ALL:
			alignment.add("A");
			break;
		default:
			alignment.add("U");
			break;
		}
		if (creature.getArmorTypes().isEmpty()) {
			ac.add(creature.getAC());
		}
		else {
			ac.add(new FvAC(creature));
		}
		hp = new FvHp(creature);
		speed = new FSpeed(creature);
		str = creature.getStrength();
		dex = creature.getDexterity();
		con = creature.getConstitution();
		myint = creature.getIntellect();
		wis = creature.getWizdom();
		cha = creature.getCharisma();
		if (!creature.getSkills().isEmpty()) {
			skill = new FvSkill(creature);
		}
		passive = creature.getPassivePerception();
		languages = new ArrayList<>(1);
		cr = creature.getChallengeRating();
		if (!creature.getFeats().isEmpty()) {
			trait = creature.getFeats().stream().map(FTrait::new).collect(Collectors.toList());
		}
		save = new FvSave(creature);
		action = creature.getActions(ActionType.ACTION).stream().map(FAction::new).collect(Collectors.toList());
		List<Action> lActions = creature.getActions(ActionType.LEGENDARY);
		if (!lActions.isEmpty()) {
			legendary = creature.getActions(ActionType.LEGENDARY).stream().map(FLegendary::new)
					.collect(Collectors.toList());
		}
		List<Action> rActions = creature.getActions(ActionType.REACTION);
		if (!rActions.isEmpty()) {
			reaction = creature.getActions(ActionType.LEGENDARY).stream().map(FAction::new)
					.collect(Collectors.toList());
		}
		
		if (!creature.getHabitates().isEmpty()) {
			environment = creature.getHabitates().stream()
					.map(HabitatType::name)
					.map(String::toLowerCase)
					.collect(Collectors.toList());
		}
		soundClip = new FSoundClip();
		senses = new ArrayList<String>(4);
		if (creature.getDarkvision() != null) {
			senses.add(String.format("darkvision %d ft.", creature.getDarkvision()));
		}
		if (creature.getBlindsight() != null) {
			senses.add(String.format("blindsight %d ft.", creature.getBlindsight()));
		}
		if (creature.getTrysight() != null) {
			senses.add(String.format("truesight %d ft.", creature.getTrysight()));
		}
		if (creature.getVibration() != null) {
			senses.add(String.format("tremorsense %d ft.", creature.getVibration()));
		}
		if (!creature.getResistanceDamages().isEmpty()) {
			resist = creature.getResistanceDamages().stream()
					.map(DamageType::name)
					.map(String::toLowerCase)
					.collect(Collectors.toList());
		}
		if (!creature.getImmunityDamages().isEmpty()) {
			immune = creature.getImmunityDamages().stream()
				.map(DamageType::name)
				.map(String::toLowerCase)
				.collect(Collectors.toList());
		}
		if (!creature.getImmunityStates().isEmpty()) {
			conditionImmune = creature.getImmunityStates().stream()
					.map(Condition::name)
					.map(String::toLowerCase)
					.collect(Collectors.toList());
		}
		for (Action action : creature.getActions()) {
			if (action.getDescription().toLowerCase().contains("дробящий урон")) {
				damageTags.add("B");
			}
			if (action.getDescription().toLowerCase().contains("колющий урон")) {
				damageTags.add("P");
			}
			if (action.getDescription().toLowerCase().contains("рубящий урон")) {
				damageTags.add("S");
			} 
			if (action.getDescription().toLowerCase().contains("урон огнём")) {
				damageTags.add("F");
			}
			if (action.getDescription().toLowerCase().contains("урон излучением")) {
				damageTags.add("R");
			}
			if (action.getDescription().toLowerCase().contains("урон некротической энергией")) {
				damageTags.add("N");
			}
		}
		if (damageTags.isEmpty()) {
			damageTags = null;
		}
	}
}