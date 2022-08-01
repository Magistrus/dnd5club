package club.dnd5.portal.dto.api.classes;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NameApi {
	@NotNull
	private String rus;
	@NotNull
	private String eng;
}