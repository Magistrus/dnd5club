package club.dnd5.portal.dto.api.bestiary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.classes.NameApi;
import club.dnd5.portal.model.creature.Creature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class BeastApi {
	private NameApi name;
	private String challengeRating;
	public BeastApi(Creature beast) {
		name = new NameApi(beast.getName(), beast.getEnglishName());
		challengeRating = beast.getChallengeRating();
	}
}
