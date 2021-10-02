package club.dnd5.portal.dto.spell;

import java.util.Set;
import java.util.stream.Collectors;

import org.thymeleaf.util.StringUtils;

import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.splells.Spell;
import groovy.transform.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpellDto {
	private int id;	
	private String level;
	private String name;
	private String englishName;
	private String ritual;
	private String school;
	private String components;
	private String timeCast;
	private String distance;
	private String duration;
	private String book;
	private String bookshort;
	private boolean homebrew;
	private Set<ShortClassDto> classes;

	private Boolean consumable = false;
	
	public SpellDto(Spell spell) {
		id = spell.getId();
		level = spell.getLevel() == 0 ? "З" : String.valueOf(spell.getLevel());
		name = StringUtils.capitalizeWords(spell.getName().toLowerCase())
				.replace(" И ", " и ").replace(" Или ", " или ").replace(" За ", " за ").replace(" С ", " с ").replace(" На ", " на ").replace(" От ", " от ").replace(" По ", " по ")
				.replace(" Над ", " над ").replace(" В ", " в ");
		ritual = String.valueOf(spell.getRitual());
		school = spell.getSchool().getName();
		timeCast = spell.getTimes().stream().map(t-> t.getNumber() + " " + t.getUnit().getName(t.getNumber())).collect(Collectors.joining(" или "));
		distance = spell.getDistance();
		duration = spell.getDuration();
		components = spell.getVerbalComponent() ? "Вербальный" + (spell.getSomaticComponent() ? ", ":"") : "";
		components += spell.getSomaticComponent() ? "Соматический" + (spell.getMaterialComponent() ? ", ":""): "";
		components += spell.getMaterialComponent() ? "Материальный " : "";
		components += spell.getAdditionalMaterialComponent() != null ? "(" + spell.getAdditionalMaterialComponent()+ ")" : "";
		consumable = spell.getConsumable();
		englishName = spell.getEnglishName();
		classes = spell.getHeroClass().stream()
				.map(ShortClassDto::new)
				.collect(Collectors.toSet());
		book = spell.getBook().getName() + (spell.getPage() != null ? ", стр. " + spell.getPage() : "");
		homebrew = spell.getBook().getType() == TypeBook.CUSTOM;
		bookshort = spell.getBook().getSource();
	}
	
	@Getter
	@NoArgsConstructor
	@EqualsAndHashCode
	private class ShortClassDto{
		private String name;
		private String englishName;
		public ShortClassDto(HeroClass heroClass) {
			name = heroClass.getName();
			englishName = heroClass.getEnglishName();
		}
	}
}