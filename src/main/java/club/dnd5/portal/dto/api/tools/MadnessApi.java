package club.dnd5.portal.dto.api.tools;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.NameValueApi;
import club.dnd5.portal.model.Dice;
import club.dnd5.portal.model.Madness;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class MadnessApi {
	private String description;
	private NameValueApi type;
	public MadnessApi(Madness madness) {
		description = madness.getDescription();
		type = new NameValueApi(madness.getMadnessType().getCyrilicName(), madness.getMadnessType().name());
		String duration;
		switch (madness.getMadnessType()) {
		case SHORT:
			duration = Dice.d10.roll() + " минут";
			break;
		case LONG:
			duration = Dice.d10.roll() * 10 + " часов";
			break;
		default:
			duration = "до излечения";
			break;
		}
		type.setAdditional(duration);
	}
}