package club.dnd5.portal.dto.api.classes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApiDto;
import club.dnd5.portal.dto.api.spell.ReferenceClassApi;
import club.dnd5.portal.model.trait.Trait;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class TraitDetailApi extends TraitApi {
	private String description;
	private List<ReferenceClassApi> classes;
	private SourceApiDto source;
	public TraitDetailApi(Trait trait) {
		super(trait);
		url = null;
		description = trait.getDescription();
		source = new SourceApiDto(trait.getBook());
	}
}