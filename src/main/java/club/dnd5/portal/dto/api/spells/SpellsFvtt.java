package club.dnd5.portal.dto.api.spells;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor

@Getter
@Setter
public class SpellsFvtt {
	private List<SpellFvtt> spell;
}