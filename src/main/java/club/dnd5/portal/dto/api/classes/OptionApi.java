package club.dnd5.portal.dto.api.classes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.classes.Option;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class OptionApi {
	protected NameApi name;
	public OptionApi(Option option) {
		name = new NameApi(option.getName(), option.getEnglishName());
	}
}