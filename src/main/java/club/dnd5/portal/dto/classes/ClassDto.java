package club.dnd5.portal.dto.classes;

import java.util.List;
import java.util.stream.Collectors;

import club.dnd5.portal.model.SpellcasterType;
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
	private List<ArchetypeDto> archetypes;

	public ClassDto(HeroClass hero) {
		id = hero.getId();
		name = hero.getCapitalazeName();
		englishName = hero.getEnglishName();
		sidekick = hero.isSidekick() ? "Напарники" : "Классы";
		hitDice = "1к" + hero.getDiceHp();
		spellcaster = hero.getSpellcasterType() != SpellcasterType.NONE;
		icon = hero.getIcon() == null ? "" : hero.getIcon();
		archetypes = hero.getArchetypes().stream().map(ArchetypeDto::new).collect(Collectors.toList());
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