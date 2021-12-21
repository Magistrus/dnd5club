package club.dnd5.portal.dto.spell;

import java.util.stream.Collectors;

import org.thymeleaf.util.StringUtils;

import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.splells.Spell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpellDto {
	private int id;	
	private int level;
	private String name;
	private String englishName;
	private boolean ritual;
	private String school;
	private String components;
	private String verbal;
	private String somatic;
	private String material;
	private String timeCast;
	private String distance;
	private String duration;
	private boolean concentration;
	private String book;
	private String bookshort;
	private boolean homebrew;
	private String classes;
	private String damageType;

	private Boolean consumable = false;
	
	public SpellDto(Spell spell) {
		id = spell.getId();
		level = spell.getLevel();
		name = StringUtils.capitalizeWords(spell.getName().toLowerCase())
				.replace(" И ", " и ").replace(" Или ", " или ").replace(" За ", " за ").replace(" С ", " с ").replace(" На ", " на ").replace(" От ", " от ").replace(" По ", " по ")
				.replace(" Над ", " над ").replace(" В ", " в ");
		ritual = spell.getRitual();
		school = spell.getSchool().getName();
		timeCast = spell.getTimes().stream().map(t-> t.getNumber() + " " + t.getUnit().getName(t.getNumber())).collect(Collectors.joining(" или "));
		distance = spell.getDistance();
		duration = spell.getDuration();
		concentration = spell.getConcentration();
		verbal = spell.getVerbalComponent() ? "В" : "";
		somatic = spell.getSomaticComponent() ? "C" : "";
		material = spell.getMaterialComponent() ? "M" : "";
		consumable = spell.getConsumable();
		englishName = spell.getEnglishName();
		homebrew = spell.getBook().getType() == TypeBook.CUSTOM;
		bookshort = spell.getBook().getSource();
	}
}