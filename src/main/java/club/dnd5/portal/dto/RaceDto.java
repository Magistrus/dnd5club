package club.dnd5.portal.dto;

import java.util.List;
import java.util.stream.Collectors;

import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.races.Race;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RaceDto {
	private Integer id;
	private String name;
	private String englishName;
	private String parentEnglishName;
	private String ability;
	private String icon;

	private boolean homebrew;
	private boolean setting;
	private String book;
	private String bookshort;
	private boolean hasSubraces;
	private List<RaceDto> subraces;
	private List<RaceDto> settingSubraces;
	private List<RaceDto> homebrewSubraces;

	public RaceDto(Race race) {
		id = race.getId();
		name = race.getCapitalazeName();
		englishName = race.getEnglishName();
		if (race.getParent()!=null) {
			parentEnglishName = race.getParent().getEnglishName().replace(" ", "_");
		}
		ability = race.getAbilityBonuses();
		icon = race.getIcon() == null ? "" : race.getIcon();
		
		hasSubraces = !race.getSubRaces().isEmpty();
		subraces = race.getSubRaces().stream().filter(a-> a.getBook().getType() == TypeBook.OFFICAL && !a.isView()).map(RaceDto::new).collect(Collectors.toList());
		settingSubraces = race.getSubRaces().stream().filter(a-> a.getBook().getType() == TypeBook.SETTING && !a.isView()).map(RaceDto::new).collect(Collectors.toList());
		homebrewSubraces = race.getSubRaces().stream().filter(a-> a.getBook().getType() == TypeBook.CUSTOM && !a.isView()).map(RaceDto::new).collect(Collectors.toList());
		homebrew = race.getBook().getType() == TypeBook.CUSTOM;
		setting = race.getBook().getType() == TypeBook.SETTING;
		book = race.getBook().getName();
		bookshort = race.getBook().getSource();
	}
}