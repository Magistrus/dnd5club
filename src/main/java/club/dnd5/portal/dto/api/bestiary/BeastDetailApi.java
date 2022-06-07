package club.dnd5.portal.dto.api.bestiary;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.ArmorType;
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
	private String hits;
	private String speed;
	private String str;
	private String dex;
	private String con;
	@JsonProperty("int")
	private String intellect;
	private String wiz;
	private String cha;

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
		hits = beast.getHpFormula();
		speed = beast.getAllSpeed();
		str = String.format("%d (%d)", beast.getStrength(), AbilityType.getModifier(beast.getStrength()));
		dex = String.format("%d (%d)", beast.getDexterity(), AbilityType.getModifier(beast.getDexterity()));
		con = String.format("%d (%d)", beast.getConstitution(), AbilityType.getModifier(beast.getConstitution()));
		intellect = String.format("%d (%d)", beast.getIntellect(), AbilityType.getModifier(beast.getIntellect()));
		wiz = String.format("%d (%d)", beast.getWizdom(), AbilityType.getModifier(beast.getWizdom()));
		cha = String.format("%d (%d)", beast.getCharisma(), AbilityType.getModifier(beast.getCharisma()));

	}
}