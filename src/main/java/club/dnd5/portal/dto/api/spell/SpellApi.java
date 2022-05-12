package club.dnd5.portal.dto.api.spell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApiDto;
import club.dnd5.portal.dto.api.classes.NameApi;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.splells.Spell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class SpellApi {
	private NameApi name;
	private byte level;
	private String school;
	private ComponentsApi components = new ComponentsApi();
	private Boolean ritual;
	private Boolean concentration;
	private String url;
	private SourceApiDto source;

	public SpellApi(Spell spell) {
		name = new NameApi(spell.getCapitalazeName(), spell.getEnglishName());
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
		url = String.format("/spells/%s", spell.getUrlName());
		if (spell.getBook().getType() == TypeBook.CUSTOM) {
			source = new SourceApiDto();
			source.setHomebrew(Boolean.TRUE);
		}
	}
}