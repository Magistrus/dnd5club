package club.dnd5.portal.dto.bestiary;

import static club.dnd5.portal.model.AbilityType.CHARISMA;
import static club.dnd5.portal.model.AbilityType.DEXTERITY;
import static club.dnd5.portal.model.AbilityType.INTELLIGENCE;
import static club.dnd5.portal.model.AbilityType.STRENGTH;
import static club.dnd5.portal.model.AbilityType.WISDOM;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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
		stealth = creature.getSkillBonus(SkillType.ATHLETICS);
		arcana = creature.getSkillBonus(SkillType.ATHLETICS);
		history = creature.getSkillBonus(SkillType.ATHLETICS);
		investigation = creature.getSkillBonus(SkillType.ATHLETICS); 
		nature = creature.getSkillBonus(SkillType.ATHLETICS);
		religion = creature.getSkillBonus(SkillType.ATHLETICS); 
		animalHandling = creature.getSkillBonus(SkillType.ATHLETICS);
		insight = creature.getSkillBonus(SkillType.ATHLETICS);
		medicine = creature.getSkillBonus(SkillType.ATHLETICS);
		perception = creature.getSkillBonus(SkillType.ATHLETICS);
		survival = creature.getSkillBonus(SkillType.ATHLETICS);
		deception = creature.getSkillBonus(SkillType.ATHLETICS);
		intimidation = creature.getSkillBonus(SkillType.ATHLETICS);
		performance = creature.getSkillBonus(SkillType.ATHLETICS);
		persuasion = creature.getSkillBonus(SkillType.ATHLETICS);
		
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
		
		creature.setDarkvision(darkvision);
		creature.setTrysight(trysight);
		creature.setVibration(vibration);
		creature.setBlindsight(blindsight);
		
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