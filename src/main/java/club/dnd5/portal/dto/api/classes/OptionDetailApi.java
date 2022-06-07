package club.dnd5.portal.dto.api.classes;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApiDto;
import club.dnd5.portal.dto.api.spell.ReferenceClassApi;
import club.dnd5.portal.model.classes.Option;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class OptionDetailApi extends OptionApi {
	private String requirements ;
	private String description;
	private SourceApiDto source;
	private List<ReferenceClassApi> classes;

	public OptionDetailApi(Option option) {
		super(option);
		url = null;
		description = option.getDescription();
		source = new SourceApiDto(option.getBook());
		requirements = option.getPrerequisite();
		classes = option.getOptionTypes().stream().map(ReferenceClassApi::new).collect(Collectors.toList());
	}
}