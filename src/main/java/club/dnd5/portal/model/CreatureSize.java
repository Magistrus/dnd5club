package club.dnd5.portal.model;

import lombok.Getter;

@Getter
public enum CreatureSize {
	TINY("Крошечный (Tiny - 1/4 клетки)","Крошечная (Tiny - 1/4 клетки)", "Крошечное (Tiny - 1/4 клетки)"), // 0
	SMALL("Маленький (Small - 1/2 клетки)", "Маленькая (Small - 1/2 клетки)", "Маленькое (Small - 1/2 клетки)"), // 1
	MEDIUM("Средний (Medium - 1 клетка)", "Средняя (Medium - 1 клетка)", "Среднее (Medium - 1 клетка)"), // 2
	LARGE("Большой (Large - 2х2 клетки)", "Большая (Large - 2х2 клетки)", "Большое (Large - 2х2 клетки)"), // 3
	HUGE("Огромный (Huge - 3х3 клетки)", "Огромная (Huge - 3х3 клетки)", "Огромное (Huge - 3х3 клетки)"), // 4
	GARGANTUAN("Громадный (Gargantuan - 4х4 клетки или больше)", "Громадная (Gargantuan)- 4х4 клетки или больше", "Громадное (Gargantuan) - 4х4 клетки или больше"); 

	private String [] names;
	CreatureSize(String... names){
		this.names = names;
	}

	public static CreatureSize parse(String size) {
		for (CreatureSize creatureSize : values()) {
			for (String sizeName : creatureSize.names) {
				if (sizeName.equalsIgnoreCase(size)) {
					return creatureSize;
				}
			}
		}
		return null;
	}

	public String getSizeName(CreatureType type) {
		switch (type) {
		case ABERRATION:
		case FEY:
		case OOZE:
		case UNDEAD:
		case SLIME:
		case SMALL_BEAST:
			return names[1];
		case FIEND:
		case MONSTROSITY:
		case PLANT:
			return names[2];
		default:
			return names[0];
		}
	}

	public String getCyrilicName() {
		return names[0];
	}
}