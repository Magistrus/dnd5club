package club.dnd5.portal.model.foundary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FDiscription {
	private String value;
	private String chat ="";
	private String unidentified ="";

	public FDiscription(String description) {
		value = description;
	}
}
