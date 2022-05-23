package club.dnd5.portal.dto.api.spell;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.classes.archetype.Archetype;
import club.dnd5.portal.model.splells.Spell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class SpellDetailApi extends SpellApi {
	private String range;
	private String duration;
	private String time;
	private List<SpellClassApi> classes;
	private List<SpellClassApi> subclasses;
	private List<SpellClassApi> races;
	private List<SpellClassApi> backgrounds;
	
	private String description;
	private String upper;
	public SpellDetailApi(Spell spell) {
		super(spell);
		range = spell.getDistance();
		duration = spell.getDuration();
		time = spell.getTimes().stream().map(t -> t.getName()).collect(Collectors.joining(" или "));
		description = spell.getDescription();
		components.setM(spell.getAdditionalMaterialComponent());
		classes = spell.getHeroClass().stream().map(SpellClassApi::new).collect(Collectors.toList());
		if (spell.getUpperLevel()!=null) {
			upper = spell.getUpperLevel();
		}
		source.setPage(spell.getPage());
	}

}