package club.dnd5.portal.model.foundary;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FBeastiary {
	List<FBeast> monster;
	public FBeastiary(List<FBeast> creatures) {
		monster = creatures;
	}
}