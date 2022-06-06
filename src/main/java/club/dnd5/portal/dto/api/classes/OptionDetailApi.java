package club.dnd5.portal.dto.api.classes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApiDto;
import club.dnd5.portal.model.classes.Option;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class OptionDetailApi extends OptionApi {
	private String description;
	private SourceApiDto source;

	public OptionDetailApi(Option option) {
		super(option);
		url = null;
		description = option.getDescription();
		source = new SourceApiDto(option.getBook());
	}
}