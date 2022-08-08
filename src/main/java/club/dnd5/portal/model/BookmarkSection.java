package club.dnd5.portal.model;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum BookmarkSection {
	MENU ("Разделы", "menu", null),
	CLASSES ("Классы", "classes", "class"),
	RACES ("Расы", "races", "race"),
	TRAITS ("Черты", "traits", "trait"),
	OPTIONS ("Особенности классов", "options", "option"),
	BACKGROUNDS ("Предыстории", "backgrounds", "background"),
	SPELLS ("Заклинания", "spells", "spell"),
	WEAPONS ("Оружие", "weapons", "weapon"),
	ARMORS ("Доспехи", "armors", "armor"),
	ITEMS ("Снаряжение", "items", null),
	MAGIC_ITEMS ("Магические предметы", "magicItems", "magic-item"),
	BESTIARY ("Бестиарий", "bestiary", "creature"),
	SCREENS ("Ширма Мастера", "screens", "screen"),
	GODS ("Боги", "gods", "god"),
	RULES ("Правила и термины", "rules", "rule"),
	BOOKS ("Источники", "books", "book"),
	NONE ("Без группы", "none", null);

	private final String name;
	private final String code;
	private final String itemType;
	private final Integer order;

	BookmarkSection(String name, String code, String itemType) {
		this.name = name;
		this.code = code;
		this.itemType = itemType;
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
