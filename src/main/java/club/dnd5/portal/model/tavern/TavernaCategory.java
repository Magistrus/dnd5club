package club.dnd5.portal.model.tavern;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TavernaCategory {
	CHEAP("Дешёвое"), 
	ORDINARY("Обычное"), 
	EXPENSIVE("Дорогое"),
	ELITE("Элитное");
	private String name;
}
