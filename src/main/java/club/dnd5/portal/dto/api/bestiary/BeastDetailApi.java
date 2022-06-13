package club.dnd5.portal.dto.api.bestiary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import club.dnd5.portal.dto.api.NameValueApi;
import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.model.ArmorType;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.Language;
import club.dnd5.portal.model.creature.Action;
import club.dnd5.portal.model.creature.ActionType;
import club.dnd5.portal.model.creature.Condition;
import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.creature.HabitatType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"name", "size", "type", "str", "dex", "con", "int", "wiz", "cha"})

@NoArgsConstructor
@Getter
@Setter
public class BeastDetailApi extends BeastApi {
	private int id;
	private Integer experience;
	private SizeApi size;
	private String alignment;
	private Byte armorClass;
	private Collection<String> armors;
	private String armorText;
	private HitPointsApi hits;
	private Collection<NameValueApi> speed;
	private AbilityApi ability;
	
	private Collection<NameValueApi> savingThrows;
	private Collection<NameValueApi> skills;
	
	private Collection<String> damageResistances;
	private Collection<String> damageImmunities;
	private Collection<String> damageVulnerabilities;
	private Collection<String> conditionImmunities;
	private SenseApi senses;
	private Collection<String> language;
	
	private Collection<NameValueApi> feats;
	private Collection<NameValueApi> actions;
	private Collection<NameValueApi> reactions;
	private Collection<NameValueApi> bonusActions;
	private LegendaryApi legendary;
	private Collection<NameValueApi> mysticalActions;
	
	private String description;
	private Collection<TagApi> tags;
	
	private Collection<String> environment;
	private Collection<String> images;
	private SourceApi source;
	private LairApi lair;
	
	public BeastDetailApi(Creature beast) {
		super(beast);
		id = beast.getId();
		size = new SizeApi(beast.getSizeName(), beast.getSize().name().toLowerCase(), beast.getSize().getCell());
		experience = beast.getExp();
		alignment = beast.getAligment();
		armorClass = beast.getAC();
		setType(new TypeDetailApi(beast));
		if (!beast.getArmorTypes().isEmpty()) {
			armors = beast.getArmorTypes().stream().map(ArmorType::getCyrillicName).collect(Collectors.toList());
		}
		if (beast.getBonusAC() != null) {
			armorText = beast.getBonusAC();
		}
		hits = new HitPointsApi(beast);
		speed = new ArrayList<>(5);
		speed.add(new NameValueApi(null, beast.getSpeed()));
		if (beast.getFlySpeed() != null) {
			NameValueApi value = new NameValueApi("летая", beast.getFlySpeed());
			if (beast.getHover() != null) {
				value.setAdditional("парит");
			}
			speed.add(value);
		}
		if (beast.getSwimmingSpped() != null) {
			speed.add(new NameValueApi("плавая", beast.getSwimmingSpped()));
		}
		if (beast.getDiggingSpeed() != null) {
			speed.add(new NameValueApi("копая ", beast.getDiggingSpeed()));
		}
		if (beast.getClimbingSpeed() != null) {
			speed.add(new NameValueApi("лазая", beast.getClimbingSpeed()));
		}
		ability = new AbilityApi(beast);
		
		if (!beast.getSavingThrows().isEmpty()) {
			savingThrows = beast.getSavingThrows().stream().map(st -> new NameValueApi(st.getAbility().getCyrilicName(), st.getBonus())).collect(Collectors.toList());
		}
		if (!beast.getSkills().isEmpty()) {
			skills = beast.getSkills().stream().map(skill -> new NameValueApi(skill.getType().getCyrilicName(), skill.getBonus())).collect(Collectors.toList());
		}
		if (!beast.getResistanceDamages().isEmpty()) {
			damageResistances = beast.getResistanceDamages().stream().map(DamageType::getCyrilicName).collect(Collectors.toList());
		}
		if (!beast.getImmunityDamages().isEmpty()) {
			damageImmunities = beast.getImmunityDamages().stream().map(DamageType::getCyrilicName).collect(Collectors.toList());
		}
		if (!beast.getVulnerabilityDamages().isEmpty()) {
			damageVulnerabilities = beast.getVulnerabilityDamages().stream().map(DamageType::getCyrilicName).collect(Collectors.toList());
		}
		if (!beast.getImmunityStates().isEmpty()) {
			conditionImmunities = beast.getImmunityStates().stream().map(Condition::getCyrilicName).collect(Collectors.toList());
		}
		senses = new SenseApi(beast);
		if (!beast.getFeats().isEmpty()) {
			feats = beast.getFeats().stream().map(feat -> new NameValueApi(feat.getName(), feat.getDescription())).collect(Collectors.toList());
		}
		Collection<Action> actionsBeast = beast.getActions(ActionType.ACTION);
		if (!actionsBeast.isEmpty()) {
			actions = actionsBeast.stream().map(action -> new NameValueApi(action.getName(), action.getDescription())).collect(Collectors.toList());
		}
		actionsBeast = beast.getActions(ActionType.REACTION);
		if (!actionsBeast.isEmpty()) {
			reactions = actionsBeast.stream().map(action -> new NameValueApi(action.getName(), action.getDescription())).collect(Collectors.toList());
		}
		actionsBeast = beast.getActions(ActionType.BONUS);
		if (!actionsBeast.isEmpty()) {
			bonusActions = actionsBeast.stream().map(action -> new NameValueApi(action.getName(), action.getDescription())).collect(Collectors.toList());
		}
		actionsBeast = beast.getActions(ActionType.LEGENDARY);
		if (!actionsBeast.isEmpty()) {
			legendary = new LegendaryApi();
			legendary.setList(actionsBeast.stream().map(action -> new NameValueApi(action.getName(), action.getDescription())).collect(Collectors.toList()));
			if (beast.getLegendary() != null) {
				legendary.setDescription(beast.getLegendary());
			}
		}
		actionsBeast = beast.getActions(ActionType.MYSTICAL);
		if (!actionsBeast.isEmpty()) {
			mysticalActions = actionsBeast.stream().map(action -> new NameValueApi(action.getName(), action.getDescription())).collect(Collectors.toList());
		}
		description = beast.getDescription();
		if (!beast.getHabitates().isEmpty()) {
			environment = beast.getHabitates().stream().map(HabitatType::getName).collect(Collectors.toList());	
		}
		source = new SourceApi(beast.getBook());
		if (beast.getLair() != null) {
			lair = new LairApi(beast.getLair());
		}
		if (!beast.getRaces().isEmpty()) {
			tags = beast.getRaces().stream().map(TagApi::new).collect(Collectors.toList());
		}
		if (!beast.getLanguages().isEmpty()) {
			language = 	beast.getLanguages().stream().map(Language::getName).collect(Collectors.toList());
		}
	}
}