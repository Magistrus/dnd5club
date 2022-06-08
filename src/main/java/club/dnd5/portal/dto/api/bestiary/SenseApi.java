package club.dnd5.portal.dto.api.bestiary;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.NameValueApi;
import club.dnd5.portal.model.creature.Creature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor

@Getter
@Setter
public class SenseApi {
	private Byte passivePerception;
	private List<NameValueApi> senses;
	public SenseApi(Creature beast) {
		passivePerception = beast.getPassivePerception();
		if (beast.getDarkvision() != null) {
			senses = new ArrayList<>(4);
			senses.add(new NameValueApi("тёмное зрение", beast.getDarkvision()));
		}
		if (beast.getTrysight() != null) {
			if (senses == null) {
				senses = new ArrayList<>(3);
			}
			senses.add(new NameValueApi("истинное зрение", beast.getTrysight()));
		}
		if (beast.getBlindsight() != null) {
			if (senses == null) {
				senses = new ArrayList<>(2);
			}
			senses.add(new NameValueApi("слепое зрение", beast.getBlindsight()));
		}
		if (beast.getVibration() != null) {
			if (senses == null) {
				senses = new ArrayList<>(1);
			}
			NameValueApi value = new NameValueApi("чувство вибрации", beast.getVibration());
			if (beast.getBlindsightRadius() != null && beast.getBlindsightRadius() == 1) {
				value.setRadius(Boolean.TRUE);
			}
			senses.add(value);
		}
		if (beast.getHover() != null) {
		}
	}
}