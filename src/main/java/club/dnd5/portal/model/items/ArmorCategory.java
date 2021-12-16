package club.dnd5.portal.model.items;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArmorCategory {
	LIGHT("Легкий доспех", "1 минута", "1 минута"),
	MEDIUM("Средний доспех", "5 минут", "1 минута"),
	HEAVY("Тяжелый доспех", "10 минут", "5 минут"),
	SHIELD("Щит", "1 действие", "1 действие");
	private String name;
	private String putting;
	private String removal;
}