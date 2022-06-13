package club.dnd5.portal.dto.api.bestiary;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.creature.Creature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class TypeDetailApi {
	private String name;
	private List<String> tags;
	public TypeDetailApi(Creature beast) {
		name = beast.getType().getCyrilicName();
		if (beast.getRaceName() != null) {
			tags = Arrays.asList(beast.getRaceName().split(","));
		}
	}
}