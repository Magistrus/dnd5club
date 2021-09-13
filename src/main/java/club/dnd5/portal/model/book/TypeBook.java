package club.dnd5.portal.model.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString

public enum TypeBook {
	OFFICAL("официальное"),
	MODULE("приключение"),
	SETTING("сеттинг"),
	CUSTOM("хомрул");

	private String name;
}