package club.dnd5.portal.dto.bestiary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;

import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.Alignment;
import club.dnd5.portal.model.ArmorType;
import club.dnd5.portal.model.CreatureSize;
import club.dnd5.portal.model.CreatureType;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.Dice;
import club.dnd5.portal.model.SkillType;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.creature.Action;
import club.dnd5.portal.model.creature.ActionType;
import club.dnd5.portal.model.creature.Condition;
import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.creature.CreatureFeat;
import club.dnd5.portal.model.creature.Lair;
import club.dnd5.portal.model.creature.SavingThrow;
import club.dnd5.portal.model.creature.Skill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BeastForm {
	private Integer id;
	@Column(nullable = false)
	private String name;
	private String altName;
	private String englishName;
	private String description;
	private CreatureSize size;
	private CreatureType type;
	private Alignment alignment;

	private byte AC;
	private String bonusAC;
	private ArmorType armorType;
	private boolean shield;

	private Short averageHp;
	private Short countDiceHp;
	private Dice diceHp;
	private Short bonusHP;
	private String suffixHP;
	
	private byte speed;
	private Short flySpeed;
	private Short hover;
	private Short swimmingSpped;
	private Short climbingSpeed;
	private Short diggingSpeed;
	private String speedText;

	private byte strength;
	private byte dexterity;
	private byte constitution;
	private byte intellect;
	private byte wizdom;
	private byte charisma;
	
	private Byte saveStrength;
	private Byte saveDexterity;
	private Byte saveConstitution;
	private Byte saveIntellect;
	private Byte saveWizdom;
	private Byte saveCharisma;
	
	private Byte athletics;
	private Byte acrobatics;
	private Byte sleightOfHand;
	private Byte stealth;
	private Byte arcana;
	private Byte history;
	private Byte investigation; 
	private Byte nature; 
	private Byte religion; 
	private Byte animalHandling;
	private Byte insight;
	private Byte medicine;
	private Byte perception;
	private Byte survival;
	private Byte deception;
	private Byte intimidation;
	private Byte performance;
	private Byte persuasion;
	
	private String raceName;
	
	private Set<DamageType> resists;
	private String resistText;
	private Set<DamageType> imuns;
	private String imunText;
	private Set<DamageType> vurs;
	private String vurnText;
	private LinkedHashSet<Condition> imunConditions;
	private String imunCondText;
	
	private Integer darkvision;
	private Integer trysight;
	private Integer vibration;
	private Integer blindsight;
	private Integer blindsightRadius;

	private byte passivePerception;

	private String challengeRating;
	
	private List<CreatureFeat> feats;
	private List<Action> actions;
	private List<Action> reactions;
	private List<Action> bonuses;
	private List<Action> legendaries;
	
	private Lair lair;

	private Book book;

	public BeastForm(Creature creature) {
		id = creature.getId();
		
		name = creature.getName();
		altName = creature.getAltName();
		englishName = creature.getEnglishName();
		raceName = creature.getRaceName();
		
		size = creature.getSize();
		type = creature.getType();
		alignment = creature.getAlignment();
		
		AC = creature.getAC();
		bonusAC = creature.getBonusAC();
		armorType = creature.getArmorTypes().isEmpty() ? null : creature.getArmorTypes().get(0);
		shield = creature.getArmorTypes().contains(ArmorType.SHIELD);
		averageHp = creature.getAverageHp();
		countDiceHp = creature.getCountDiceHp();
		diceHp = creature.getDiceHp();
		bonusHP = creature.getBonusHP();
		suffixHP = creature.getSuffixHP();
		
		speed = creature.getSpeed();
		flySpeed = creature.getFlySpeed();
		hover = creature.getHover();
		swimmingSpped = creature.getSwimmingSpped();
		climbingSpeed = creature.getClimbingSpeed();
		diggingSpeed = creature.getDiggingSpeed();
		speedText = creature.getSpeedText();

		strength = creature.getStrength();
		dexterity = creature.getDexterity();
		constitution = creature.getConstitution();
		intellect = creature.getIntellect();
		wizdom = creature.getWizdom();
		charisma = creature.getCharisma();
		
		saveStrength = creature.getSavingThrow(AbilityType.STRENGTH);
		saveDexterity = creature.getSavingThrow(AbilityType.DEXTERITY);
		saveConstitution = creature.getSavingThrow(AbilityType.CONSTITUTION);
		saveIntellect = creature.getSavingThrow(AbilityType.INTELLIGENCE);
		saveWizdom = creature.getSavingThrow(AbilityType.WISDOM);
		saveCharisma = creature.getSavingThrow(AbilityType.CHARISMA);
		
		athletics = creature.getSkillBonus(SkillType.ATHLETICS);
		acrobatics = creature.getSkillBonus(SkillType.ACROBATICS);
		sleightOfHand = creature.getSkillBonus(SkillType.SLEIGHT_OF_HAND);
		stealth = creature.getSkillBonus(SkillType.STEALTH);
		arcana = creature.getSkillBonus(SkillType.ARCANA);
		history = creature.getSkillBonus(SkillType.HISTORY);
		investigation = creature.getSkillBonus(SkillType.INVESTIGATION); 
		nature = creature.getSkillBonus(SkillType.NATURE);
		religion = creature.getSkillBonus(SkillType.RELIGION); 
		animalHandling = creature.getSkillBonus(SkillType.ANIMAL_HANDLING);
		insight = creature.getSkillBonus(SkillType.INSIGHT);
		medicine = creature.getSkillBonus(SkillType.MEDICINE);
		perception = creature.getSkillBonus(SkillType.PERCEPTION);
		survival = creature.getSkillBonus(SkillType.SURVIVAL);
		deception = creature.getSkillBonus(SkillType.DECEPTION);
		intimidation = creature.getSkillBonus(SkillType.INTIMIDATION);
		performance = creature.getSkillBonus(SkillType.PERFORMANCE);
		persuasion = creature.getSkillBonus(SkillType.PERSUASION);
		
		darkvision = creature.getDarkvision();
		trysight = creature.getTrysight();
		vibration = creature.getVibration();
		blindsight = creature.getBlindsight();
		blindsightRadius = creature.getBlindsightRadius();
		
		resists = new LinkedHashSet<>(creature.getResistanceDamages());
		imuns = new LinkedHashSet<>(creature.getImmunityDamages());
		vurs = new LinkedHashSet<>(creature.getVulnerabilityDamages());
		imunConditions = new LinkedHashSet<>(creature.getImmunityStates());
		passivePerception = creature.getPassivePerception();

		challengeRating = creature.getChallengeRating();
		
		feats = creature.getFeats();
		actions = creature.getActions(ActionType.ACTION);
		reactions = creature.getActions(ActionType.REACTION);
		bonuses = creature.getActions(ActionType.BONUS);
		legendaries = creature.getActions(ActionType.LEGENDARY);
		
		description = creature.getDescription();

		lair = creature.getLair();
		book = creature.getBook();
	}

	public Creature build() {
		Creature creature = new Creature();
		creature.setId(id);
		creature.setName(name);
		creature.setAltName(altName);
		creature.setRaceName(raceName);
		
		creature.setAC(AC);
		creature.setBonusAC(bonusAC);
		List<ArmorType> armors = new ArrayList<>(2);
		if (armorType != null) {
			armors.add(armorType);
		}
		if (shield) {
			armors.add(ArmorType.SHIELD);
		}
		
		creature.setArmorTypes(armors);

		creature.setStrength(strength);
		creature.setDexterity(dexterity);
		creature.setConstitution(constitution);
		creature.setIntellect(intellect);
		creature.setWizdom(wizdom);
		creature.setCharisma(charisma);
		
		ArrayList<SavingThrow> savingThrows = new ArrayList<SavingThrow>();
		if (saveStrength != null) {
			savingThrows.add(new SavingThrow(AbilityType.STRENGTH, saveStrength));
		}
		if (saveConstitution != null) {
			savingThrows.add(new SavingThrow(AbilityType.CONSTITUTION, saveConstitution));
		}
		if (saveDexterity != null) {
			savingThrows.add(new SavingThrow(AbilityType.DEXTERITY, saveDexterity));
		}
		if (saveIntellect != null) {
			savingThrows.add(new SavingThrow(AbilityType.INTELLIGENCE, saveIntellect));
		}
		if (saveWizdom != null) {
			savingThrows.add(new SavingThrow(AbilityType.WISDOM, saveWizdom));
		}
		if (saveCharisma != null) {
			savingThrows.add(new SavingThrow(AbilityType.CHARISMA, saveCharisma));
		}
		creature.setSavingThrows(savingThrows);
		 
		List<Skill> skills = new ArrayList<>();
		if (athletics != null) {
			skills.add(new Skill(SkillType.ATHLETICS, athletics));
		}
		if (acrobatics != null) {
			skills.add(new Skill(SkillType.ACROBATICS, acrobatics));
		}
		if (stealth != null) {
			skills.add(new Skill(SkillType.STEALTH, strength));
		}
		if (arcana != null) {
			skills.add(new Skill(SkillType.ARCANA, arcana));
		}
		if (history != null) {
			skills.add(new Skill(SkillType.HISTORY, history));
		}
		if (investigation != null) {
			skills.add(new Skill(SkillType.INVESTIGATION, investigation));
		}
		if (nature != null) {
			skills.add(new Skill(SkillType.NATURE, nature));
		}
		if (religion != null) {
			skills.add(new Skill(SkillType.RELIGION, religion));
		}
		if (animalHandling != null) {
			skills.add(new Skill(SkillType.ANIMAL_HANDLING, animalHandling));
		}
		if (insight != null) {
			skills.add(new Skill(SkillType.INSIGHT, insight));
		}
		if (medicine != null) {
			skills.add(new Skill(SkillType.MEDICINE, medicine));
		}
		if (perception != null) {
			skills.add(new Skill(SkillType.PERCEPTION, perception));
		}
		if (survival != null) {
			skills.add(new Skill(SkillType.SURVIVAL, survival));
		}
		if (deception != null) {
			skills.add(new Skill(SkillType.DECEPTION, deception));
		}
		if (intimidation != null) {
			skills.add(new Skill(SkillType.INTIMIDATION, intimidation));
		}
		if (performance != null) {
			skills.add(new Skill(SkillType.PERFORMANCE, performance));
		}
		if (persuasion != null) {
			skills.add(new Skill(SkillType.PERSUASION, persuasion));
		}
		creature.setSkills(skills);
		
		creature.setDarkvision(darkvision);
		creature.setTrysight(trysight);
		creature.setVibration(vibration);
		creature.setBlindsight(blindsight);

		List<Action> storeActions = new ArrayList<Action>();
		if (actions != null) {
			storeActions.addAll(actions);	
		}
		if (reactions != null) {
			storeActions.addAll(reactions);	
		}
		if (bonuses != null) {
			storeActions.addAll(bonuses);	
		}
		if (legendaries != null) {
			storeActions.addAll(legendaries);
		}
		creature.setActions(storeActions);
		creature.setFeats(feats);
		
		List<DamageType> resistDamages = Arrays.stream(resistText.split("\\|"))
				.map(DamageType::valueOf)
				.collect(Collectors.toList());
		creature.setResistanceDamages(resistDamages);
		List<DamageType> immunDamages = Arrays.stream(imunText.split("\\|"))
				.map(DamageType::valueOf)
				.collect(Collectors.toList());
		creature.setImmunityDamages(immunDamages);
		List<DamageType> vurnDamages = Arrays.stream(vurnText.split("\\|"))
				.map(DamageType::valueOf)
				.collect(Collectors.toList());
		creature.setVulnerabilityDamages(vurnDamages);
		
		List<Condition> immunityStates = Arrays.stream(imunCondText.split("\\|"))
				.map(Condition::valueOf)
				.collect(Collectors.toList());
		creature.setImmunityStates(immunityStates);
		
		creature.setChallengeRating(challengeRating);
		
		return creature;
	}
	public void addFea(CreatureFeat creatureFeat) {
		feats.add(creatureFeat);
	}

	public void addAction(Action action) {
		actions.add(action);
	}
	
	public void addRection(Action action) {
		reactions.add(action);
	}
	
	public void addBonus(Action action) {
		bonuses.add(action);
	}
	
	public int getBonusHpAbs() {
		return Math.abs(bonusHP);
	}
	public String getResistText() {
		return resists.stream().map(DamageType::name).collect(Collectors.joining("|"));
	}
	public String getImunText() {
		return imuns.stream().map(DamageType::name).collect(Collectors.joining("|"));
	}
	public String getVurnText() {
		return vurs.stream().map(DamageType::name).collect(Collectors.joining("|"));
	}
	public String getImunCondText() {
		return imunConditions.stream().map(Condition::name).collect(Collectors.joining("|"));
	}
}	