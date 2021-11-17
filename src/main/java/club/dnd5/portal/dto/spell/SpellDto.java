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
	private int level;
	private String name;
	private String englishName;
	private boolean ritual;
	private String school;
	private String components;
	private String timeCast;
	private String distance;
	private String duration;
	private boolean concentration;
	private String book;
	private String bookshort;
	private boolean homebrew;
	private Set<ShortClassDto> classes;
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
		components = spell.getVerbalComponent() ? "<span class=\"tip\" title=\"Большинство заклинаний требуют произношения таинственных слов.<br> Сами по себе слова не являются источником силы заклинания;<br> просто комбинация звуков с особой тональностью вызывает резонанс в прядях магии, приводя их в движение.<br> Таким образом, персонаж с кляпом во рту или в области заклинания тишина,<br> не может активировать заклинания с вербальным компонентом.\">Вербальный</span>" + (spell.getSomaticComponent() ? ", ":"") : "";
		components += spell.getSomaticComponent() ? "<span class=\"tip\" title=\"Заклинание может требовать энергичной жестикуляции или замысловатой последовательности телодвижений.<br> Если у заклинания есть соматический компонент,<br> у заклинателя должна быть свободной хотя бы одна рука для исполнения этих жестов.\">Соматический</span>" + (spell.getMaterialComponent() ? ", ":""): "";
		components += spell.getMaterialComponent() ? "<span class=\"tip\" title=\"У заклинателя должна быть одна свободная рука для доступа к материальным компонентам,<br> но это может быть та же самая рука, что используется для выполнения соматического компонента.<br> Персонаж может использовать мешочек с компонентами<br> или заклинательную фокусировку вместо указанных компонентов.<br> Однако, если для компонента указана цена,<br> у персонажа для накладывания заклинания должен быть именно такой компонент.\">Материальный </span>" : "";
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