package club.dnd5.portal.model.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString

public enum TypeBook {
	OFFICAL("Основное"),
	MODULE("Приключения"),
	SETTING("Сеттинги"),
	TEST("Тестовый материал"),
	CUSTOM("Homebrew");

	private String name;
}