package club.dnd5.portal.model.foundary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FAC {
	private byte flat;
	private String calc = "default";
	private String formula;
	private byte min;
}