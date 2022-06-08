package club.dnd5.portal.dto.api.bestiary;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.hql.internal.ast.tree.MapValueNode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.NameValueApi;
import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.ArmorType;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.creature.Creature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class BeastDetailApi extends BeastApi {
	private Integer experience;
	private String size;
	private String alignment;
	private Byte armorClass;
	private List<String> armors;
	private String armorText;
	private HitPointsApi hits;
	private String speed;
	private String str;
	private String dex;
	private String con;
	@JsonProperty("int")
	private String intellect;
	private String wiz;
	private String cha;
	
	private List<NameValueApi> savingThrows;
	private List<NameValueApi> skills;
	private List<String> damageResistances;
	
	private String description;

	public BeastDetailApi(Creature beast) {
		super(beast);
		size = beast.getSizeName();
		experience = beast.getExp();
		alignment = beast.getAligment();
		armorClass = beast.getAC();
		
		if (!beast.getArmorTypes().isEmpty()) {
			armors = beast.getArmorTypes().stream().map(ArmorType::getCyrillicName).collect(Collectors.toList());
		}
		if (beast.getBonusAC() != null) {
			armorText = beast.getBonusAC();
		}
		hits = new HitPointsApi(beast);
		speed = beast.getAllSpeed();
		str = String.format("%d (%d)", beast.getStrength(), AbilityType.getModifier(beast.getStrength()));
		dex = String.format("%d (%d)", beast.getDexterity(), AbilityType.getModifier(beast.getDexterity()));
		con = String.format("%d (%d)", beast.getConstitution(), AbilityType.getModifier(beast.getConstitution()));
		intellect = String.format("%d (%d)", beast.getIntellect(), AbilityType.getModifier(beast.getIntellect()));
		wiz = String.format("%d (%d)", beast.getWizdom(), AbilityType.getModifier(beast.getWizdom()));
		cha = String.format("%d (%d)", beast.getCharisma(), AbilityType.getModifier(beast.getCharisma()));
		if (!beast.getSavingThrows().isEmpty()) {
			savingThrows = beast.getSavingThrows().stream().map(st -> new NameValueApi(st.getAbility().getCyrilicName(), st.getBonus())).collect(Collectors.toList());
		}
		if (!beast.getSkills().isEmpty()) {
			skills = beast.getSkills().stream().map(skill -> new NameValueApi(skill.getCyrilicText(), skill.getBonus())).collect(Collectors.toList());
		}
		if (!beast.getResistanceDamages().isEmpty()) {
			damageResistances = beast.getResistanceDamages().stream().map(DamageType::getCyrilicName).collect(Collectors.toList());
		}
		description = beast.getDescription();
	}
}