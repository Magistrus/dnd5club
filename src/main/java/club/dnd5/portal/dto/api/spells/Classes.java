package club.dnd5.portal.dto.api.spells;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import club.dnd5.portal.model.classes.HeroClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Classes {
	private List<FromClassList> fromClassList = new ArrayList<FromClassList>();

	public Classes(List<HeroClass> heroClass) {
		this.fromClassList = heroClass.stream().map(FromClassList::new).collect(Collectors.toList());
	}
}