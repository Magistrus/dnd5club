package club.dnd5.portal.dto.name;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RaceNameDto implements IRaceName {
	private int raceId;
	private String name;
	private String bookType;
}