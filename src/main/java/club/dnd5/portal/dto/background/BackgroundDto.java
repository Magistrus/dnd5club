package club.dnd5.portal.dto.background;

import java.util.stream.Collectors;

import org.thymeleaf.util.StringUtils;

import club.dnd5.portal.model.SkillType;
import club.dnd5.portal.model.background.Background;
import club.dnd5.portal.model.book.TypeBook;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BackgroundDto {
	private Integer id;
	private String name;
	private String englishName;
	private String skills;
	private String toolSkills;
	private String languages;
	private String equipments;
	private int startMoney;
	private boolean homebrew;
	private String book;
	private String bookshort;
	
	public BackgroundDto(Background background) {
		id = background.getId();
		name = StringUtils.capitalizeWords(background.getName().toLowerCase())
				.replace(" И ", " и ").replace(" Или ", " или ").replace(" За ", " за ").replace(" С ", " с ").replace(" На ", " на ").replace(" От ", " от ").replace(" По ", " по ")
				.replace(" Над ", " над ").replace(" В ", " в ");
		englishName = background.getEnglishName();
		skills = background.getSkills().stream().map(SkillType::getCyrilicName).collect(Collectors.joining(", "));
		if (background.getOtherSkills() != null) {
			skills+= " " + background.getOtherSkills();
		}
		toolSkills = background.getToolOwnership() == null ? "Нет" : background.getToolOwnership();
		languages = background.getLanguage() == null ? "-" : background.getLanguage();
		equipments = background.getEquipmentsText() == null ? "-": background.getEquipmentsText();
		startMoney = background.getStartMoney();
		homebrew = background.getBook().getType() == TypeBook.CUSTOM;
		book = background.getBook().getName();
		bookshort = background.getBook().getSource();
	}
}