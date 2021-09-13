package club.dnd5.portal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeUnit {
	BONUS("бонусное действие"), 
	REACTION("реакция"),
	ACTION("действие"), 
	ROUND("ход"),
	MINUTE("минута"),
	HOUR("час");

	private String name;

	public String getName(int number) {
		switch (this) {
		case MINUTE:
			if (number == 10) {
				return "минут";
			} else {
				return name;
			}
		case HOUR:
			if (number == 8 || number == 12) {
				return "часов";
			} else if (number == 24) {
				return "часа";
			} else
				return name;
		default:
			return name;
		}
	}
}