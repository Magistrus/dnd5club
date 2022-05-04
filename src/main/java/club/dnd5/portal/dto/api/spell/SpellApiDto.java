package club.dnd5.portal.dto.api.spell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApiDto;
import club.dnd5.portal.dto.api.classes.NameApiDto;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.splells.Spell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class SpellApiDto {
	private NameApiDto name;
	private byte level;
	private String school;
	private ComponentsApiDto components = new ComponentsApiDto();
	private Boolean ritual;
	private Boolean concentration;
	private SourceApiDto source;

	public SpellApiDto(Spell spell) {
		name = new NameApiDto(spell.getCapitalazeName(), spell.getEnglishName());
		level = spell.getLevel();
		school = spell.getSchool().getName();
		if (spell.getVerbalComponent()) {
			components.setV(Boolean.TRUE);	
		}
		if (spell.getSomaticComponent()) {
			components.setS(Boolean.TRUE);	
		}
		if (spell.getMaterialComponent()) {
			components.setM(Boolean.TRUE);
		}
		if (spell.getRitual()) {
			ritual = Boolean.TRUE;
		}
		if (spell.getConcentration()) {
			concentration = Boolean.TRUE;
		}
		if (spell.getBook().getType() == TypeBook.CUSTOM) {
			source = new SourceApiDto();
			source.setHomebrew(Boolean.TRUE);
		}
	}
}