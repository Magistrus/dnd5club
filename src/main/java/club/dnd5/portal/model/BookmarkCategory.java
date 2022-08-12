package club.dnd5.portal.model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public enum BookmarkCategory {
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

	BookmarkCategory(String name, String code, String itemType) {
		this.name = name;
		this.code = code;
		this.itemType = itemType;
		this.order = ordinal();
	}

	public static List<BookmarkCategory> getCategories() {
		return Arrays.asList(values());
	}

	public static BookmarkCategory getDefaultCategory() {
		return NONE;
	}

	public static BookmarkCategory getCategoryByURL(@NotNull String url) {
		String[] paths = Arrays.stream(url.split("/"))
			.filter(str -> !str.isEmpty())
			.toArray(String[]::new);

		if (paths[0].isEmpty()) {
			return getDefaultCategory();
		}

		if (paths[0].equals("items") && paths[1].equals("magic")) {
			return MAGIC_ITEMS;
		}

		Optional<BookmarkCategory> bookmark = getCategories()
			.stream()
			.filter(section -> section.getCode().equals(paths[0]))
			.findFirst();

		return bookmark.orElse(getDefaultCategory());
	}

	public static BookmarkCategory getCategoryByCode(@NotNull String code) {
		Optional<BookmarkCategory> bookmark = getCategories()
			.stream()
			.filter(section -> section.getCode().equals(code))
			.findFirst();

		return bookmark.orElse(getDefaultCategory());
	}
}
