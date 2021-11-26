package club.dnd5.portal.model.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString

public enum TypeBook {
	OFFICAL("Официальные издания"),
	MODULE("Приключения"),
	SETTING("Официальные сеттинги"),
	CUSTOM("Не официальное (homebrew)");

	private String name;
}