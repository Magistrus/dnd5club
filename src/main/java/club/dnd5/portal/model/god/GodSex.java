package club.dnd5.portal.model.god;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GodSex {
	MALE("бог"),
	FEMALE("богиня"),
	PHILOSOPHY("философия"),
	UNDEFINE("божество");
	
	private String cyrilicName;
	
	public static GodSex parse(String sex) {
		for (GodSex godSex : values()) {
			if (godSex.cyrilicName.equals(sex)) {
				return godSex;
			}
		}
		return UNDEFINE;
	}
}