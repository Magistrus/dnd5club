package club.dnd5.portal.model.foundary;

import lombok.Getter;

@Getter
public class FFly {
	private short number;
	private String condition = "(hover)";
	public FFly(short number) {
		this.number = number;
	}
}