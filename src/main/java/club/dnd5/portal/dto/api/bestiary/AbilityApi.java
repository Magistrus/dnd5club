package club.dnd5.portal.dto.api.bestiary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import club.dnd5.portal.model.creature.Creature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"str", "dex", "con", "int", "wiz", "cha"})

@NoArgsConstructor
@Getter
@Setter
public class AbilityApi {
	private int str;
	private int dex;
	private int con;
	@JsonProperty("int")
	private int intellect;
	private int wiz;
	private int cha;
	public AbilityApi(Creature beast) {
		str = beast.getStrength();
		dex = beast.getDexterity();
		con = beast.getConstitution();
		intellect = beast.getIntellect();
		wiz = beast.getWizdom();
		cha = beast.getCharisma();
	}
}