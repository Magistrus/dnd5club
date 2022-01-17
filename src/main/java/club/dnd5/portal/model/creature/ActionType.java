package club.dnd5.portal.model.creature;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActionType {
	ACTION("Действия"),
	REACTION("Реакции"),
	LEGENDARY("Легендарные действия"),
	BONUS("Бонусные действия"),
	LAIR("Действия логова");
	private String name;
}