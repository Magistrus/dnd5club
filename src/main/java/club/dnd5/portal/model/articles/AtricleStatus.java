package club.dnd5.portal.model.articles;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AtricleStatus {
	CREATED("Создана"),
	MODERATION("На модерации"),
	PUBLISHED("Опубликована"),
	CANCELED("Отклонена"),
	REMOVED("Удалена");
	private String name;
}