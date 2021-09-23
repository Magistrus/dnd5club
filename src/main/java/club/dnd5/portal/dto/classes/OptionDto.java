package club.dnd5.portal.dto.classes;

import java.util.List;
import java.util.stream.Collectors;

import org.thymeleaf.util.StringUtils;

import club.dnd5.portal.model.classes.Option;
import club.dnd5.portal.model.classes.Option.OptionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OptionDto {
	private int id; 
	private String name;
	private String englishName;
	private List<String> optionTypes;
	private String type;
	private String prerequisite;
	private Integer level;
	private String book;
	private String bookshort;
	
	public OptionDto(Option option) {
		id = option.getId();
		name = StringUtils.capitalizeWords(option.getName().toLowerCase())
				.replace(" И ", " и ").replace(" Или ", " или ").replace(" За ", " за ").replace(" С ", " с ").replace(" На ", " на ").replace(" От ", " от ").replace(" По ", " по ")
				.replace(" Над ", " над ").replace(" В ", " в ");
		englishName = option.getEnglishName();
		optionTypes = option.getOptionTypes().stream()
				.map(OptionType::getShortName)
				.collect(Collectors.toList());
		type = option.getOptionTypes().stream()
				.map(OptionType::getName)
				.collect(Collectors.joining(", "));
		prerequisite = option.getPrerequisite();
		level = option.getLevel();
		book = option.getBook().getName();
		bookshort = option.getBook().getSource();
	}
}