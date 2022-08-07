package club.dnd5.portal.model;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum BookmarkSection {
	MENU ("Разделы", "menu"),
	CLASSES ("Классы", "classes"),
	RACES ("Расы", "races"),
	TRAITS ("Черты", "traits"),
	OPTIONS ("Особенности классов", "options"),
	BACKGROUNDS ("Предыстории", "backgrounds"),
	SPELLS ("Заклинания", "spells"),
	WEAPONS ("Оружие", "weapons"),
	ARMORS ("Доспехи", "armors"),
	ITEMS ("Снаряжение", "items"),
	MAGIC_ITEMS ("Магические предметы", "magicItems"),
	BESTIARY ("Бестиарий", "bestiary"),
	SCREENS ("Ширма Мастера", "screens"),
	GODS ("Боги", "gods"),
	RULES ("Правила и термины", "rules"),
	BOOKS ("Источники", "books"),
	NONE ("Без группы", "none");

	@Getter() private final String name;
	@Getter() private final String code;
	@Getter() private final Integer order;

	BookmarkSection(String name, String code) {
		this.name = name;
		this.code = code;
		this.order = ordinal();
	}

	public static List<BookmarkSection> getSections() {
		return Stream.of(BookmarkSection.values())
			.collect(Collectors.toList());
	}

	public static BookmarkSection getDefaultSection() {
		return NONE;
	}

	public static BookmarkSection getSectionByURL(@NotNull String url) {
		String[] paths = url.split("/");

		if (paths[0].isEmpty()) {
			return getDefaultSection();
		}

		if (paths[0].equals("items") && paths[1].equals("magic")) {
			return MAGIC_ITEMS;
		}

		Optional<BookmarkSection> bookmark = getSections()
			.stream()
			.filter(section -> section.getCode().equals(paths[0]))
			.findFirst();

		return bookmark.orElse(getDefaultSection());

	}
}
