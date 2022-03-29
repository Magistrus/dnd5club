package club.dnd5.portal.dto.api.spells;

import club.dnd5.portal.model.classes.HeroClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FromClassList {
    private String name;
    private String source;
    public FromClassList(HeroClass heroClass) {
		this.name = heroClass.getEnglishName();
		this.source = heroClass.getBook().getSource();
	}
}