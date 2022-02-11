package club.dnd5.portal.dto.classes;

import java.util.List;
import java.util.stream.Collectors;

import club.dnd5.portal.model.SpellcasterType;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.classes.archetype.Archetype;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClassDto {
	private Integer id;
	private String name;
	private String englishName;
	private String sidekick;
	private String hitDice;
	private boolean spellcaster;
	private String icon;
	private String book;
	private String bookshort;
	private String archetypeName;
	private List<ArchetypeDto> archetypes;
	private List<ArchetypeDto> settingArchetypes;
	private List<ArchetypeDto> homebrewArchetypes;

	public ClassDto(HeroClass hero) {
		id = hero.getId();
		name = hero.getCapitalazeName();
		englishName = hero.getEnglishName();
		archetypeName = hero.getArchetypeName();
		sidekick = hero.isSidekick() ? "Напарники" : "";
		hitDice = "1к" + hero.getDiceHp();
		spellcaster = hero.getSpellcasterType() != SpellcasterType.NONE;
		icon = hero.getIcon() == null ? "" : hero.getIcon();
		archetypes = hero.getArchetypes().stream().filter(a->a.getBook().getType() == TypeBook.OFFICAL).map(ArchetypeDto::new).collect(Collectors.toList());
		settingArchetypes = hero.getArchetypes().stream().filter(a->a.getBook().getType() == TypeBook.SETTING).map(ArchetypeDto::new).collect(Collectors.toList());
		homebrewArchetypes = hero.getArchetypes().stream().filter(a->a.getBook().getType() == TypeBook.CUSTOM).map(ArchetypeDto::new).collect(Collectors.toList());
		book = hero.getBook().getName();
		bookshort = hero.getBook().getSource();
	}

	@Getter
	@Setter
	class ArchetypeDto{
		private Integer id;
		private String name;
		private String englishName;
		private String book;
		private String bookshort;

		public ArchetypeDto(Archetype archetype) {
			id = archetype.getId();
			name= archetype.getCapitalizeName();
			englishName = archetype.getEnglishName();
			book = archetype.getBook().getName();
			bookshort = archetype.getBook().getSource();
		}
	}
}