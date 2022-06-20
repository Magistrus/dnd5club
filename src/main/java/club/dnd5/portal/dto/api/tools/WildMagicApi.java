package club.dnd5.portal.dto.api.tools;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.model.splells.WildMagic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class WildMagicApi {
	private String description;
	private SourceApi source;
	public WildMagicApi(WildMagic wildMagic) {
		description = wildMagic.getDescription();
		source = new  SourceApi(wildMagic.getBook());
	}
}