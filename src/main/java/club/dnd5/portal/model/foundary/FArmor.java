package club.dnd5.portal.model.foundary;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
public class FArmor {
	private byte value = 10;
	private String type;
	private Byte dex;
}