package club.dnd5.portal.dto;

import java.util.stream.Collectors;

import org.thymeleaf.util.StringUtils;

import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.splells.Spell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpellDto {
	private int id;	
	private String level;
	private String name;
	private String ritual;
	private String concentration;
	private String school;
	private String components;
	private String timeCast;
	private String distance;
	private String duration;
	private String upperLevel;
	private String book;
	private String englishName;
	private Boolean consumable = false;
	
	public SpellDto(Spell spell) {
		id = spell.getId();
		level = spell.getLevel() == 0 ? "Заговор" : String.valueOf(spell.getLevel()) + " уровень";
		name = StringUtils.capitalizeWords(spell.getName().toLowerCase())
				.replace(" И ", " и ").replace(" Или ", " или ").replace(" За ", " за ").replace(" С ", " с ").replace(" На ", " на ").replace(" От ", " от ").replace(" По ", " по ")
				.replace(" Над ", " над ").replace(" В ", " в ");
		ritual = String.valueOf(spell.getRitual());
		concentration = String.valueOf(spell.getConcentration());
		school = spell.getSchool().getName();
		timeCast = spell.getTimes().stream().map(t-> t.getNumber() + " " + t.getUnit().getName(t.getNumber())).collect(Collectors.joining(" или "));
		distance = spell.getDistance();
		duration = spell.getDuration();
		components = spell.getVerbalComponent() ? "Вербальный" + (spell.getSomaticComponent() ? ", ":"") : "";
		components += spell.getSomaticComponent() ? "Соматический" + (spell.getMaterialComponent() ? ", ":""): "";
		components += spell.getMaterialComponent() ? "Материальный " : "";
		components += spell.getAdditionalMaterialComponent() != null ? "(" + spell.getAdditionalMaterialComponent()+ ")" : "";
		consumable = spell.getConsumable();
		englishName = spell.getEnglishName();
		book = spell.getBook().getName() + (spell.getPage() != null ? ", стр. " + spell.getPage() : "");
	}
}