package club.dnd5.portal.dto.spell;

import org.thymeleaf.util.StringUtils;

import club.dnd5.portal.model.splells.Spell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpellTipDto {
	private int id;	
	private String level;
	private String name;
	private String englishName;
	private String description;
	
	public SpellTipDto(Spell spell) {
		id = spell.getId();
		level = spell.getLevel() == 0 ? "ЗГ" : String.valueOf(spell.getLevel());
		name = StringUtils.capitalizeWords(spell.getName().toLowerCase())
				.replace(" И ", " и ").replace(" Или ", " или ").replace(" За ", " за ").replace(" С ", " с ").replace(" На ", " на ").replace(" От ", " от ").replace(" По ", " по ")
				.replace(" Над ", " над ").replace(" В ", " в ");
		englishName = spell.getEnglishName();
		description = spell.getDescription();
	}
}