package club.dnd5.portal.dto.api.classes;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.spells.SpellFilter;
import club.dnd5.portal.model.SpellcasterType;
import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.classes.archetype.Archetype;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class ClassInfoApiDto extends ClassApi {
	private NameApi name;
	private Collection<ClassTabApiDto> tabs = new ArrayList<>(5);
	private Collection<String> images;
	private SpellFilter customFilter;
	private String archetypeName;

	public ClassInfoApiDto(HeroClass heroClass, Collection<String> images, ClassRequestApi request) {
		super(heroClass, request);
		url = null;
		this.images = images;
		tabs.add(new ClassTabApiDto("Навыки", String.format("/classes/fragment/%s", heroClass.getUrlName()), "traits", 0, true));
		tabs.add(new ClassTabApiDto("Описание", String.format("/classes/%s/description", heroClass.getUrlName()), "description", 1, true));
		if (heroClass.getSpellcasterType() != null && heroClass.getSpellcasterType() != SpellcasterType.NONE) {
			tabs.add(new ClassTabApiDto("Заклинания", String.format("/filters/spells/%s", heroClass.getUrlName()), "spells", 2, false));
		}
		if (heroClass.getOptionType() != null) {
			tabs.add(new ClassTabApiDto(heroClass.getOptionType().getDisplayName(), String.format("/filters/options/%s", heroClass.getUrlName()), "options", 3, false));
		}
		if (heroClass.getArchetypeName() != null) {
			archetypeName = heroClass.getArchetypeName();
		}
	}

	public ClassInfoApiDto(Archetype archetype, Collection<String> images, ClassRequestApi request) {
		super(archetype.getHeroClass(), request);
		HeroClass heroClass = archetype.getHeroClass();
		this.images = images;
		tabs.add(new ClassTabApiDto("Навыки", String.format("/classes/%s/architypes/%s", heroClass.getUrlName(), archetype.getUrlName()), "traits", 0, true));
		tabs.add(new ClassTabApiDto("Описание", String.format("/classes/%s/archetype/%s/description", heroClass.getUrlName(), archetype.getUrlName()), "description", 1, true));
		if (heroClass.getSpellcasterType() != SpellcasterType.NONE || archetype.getSpellcasterType() != null) {
			tabs.add(new ClassTabApiDto("Заклинания", String.format("/filters/spells/%s/%s", heroClass.getUrlName(), archetype.getUrlName()), "spells", 2, false));
		}
		if (heroClass.getOptionType() != null) {
			tabs.add(new ClassTabApiDto(heroClass.getOptionType().getDisplayName(), String.format("/filters/options/%s", heroClass.getUrlName()), "options", 3, false));
		}
		if (archetype.getOptionType() != null) {
			tabs.add(new ClassTabApiDto(archetype.getOptionType().getDisplayName(), String.format("/filters/options/%s/%s", heroClass.getUrlName(), archetype.getUrlName()), "options", 4, false));
		}
	}
}