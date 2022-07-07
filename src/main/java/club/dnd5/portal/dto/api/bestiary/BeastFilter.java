package club.dnd5.portal.dto.api.bestiary;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import club.dnd5.portal.model.CreatureSize;
import club.dnd5.portal.model.CreatureType;
import club.dnd5.portal.model.creature.HabitatType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class BeastFilter {
	@JsonProperty("challengeRating")
	private List<String> challengeRatings;
	@JsonProperty("type")
	private List<CreatureType> types;
	
	@JsonProperty("size")
	private List<CreatureSize> sizes;
	@JsonProperty("tag")
	private List<Integer> tags;
	private List<String> moving;
	private List<String> senses;
	private List<String> features;
	@JsonProperty("environment")
	private List<HabitatType> environments;

	
	@JsonProperty("book")
	private List<String> books;
}