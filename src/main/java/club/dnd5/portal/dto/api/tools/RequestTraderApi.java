package club.dnd5.portal.dto.api.tools;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class RequestTraderApi {
	@Schema(description = "The amount of magic in the world", defaultValue = "2")
	private Integer magicLevel;
	@Schema(description = "The Persuasion (Charisma) roll results", defaultValue = "10")
	private Integer persuasion;
	@Schema(description = "Show only unique magic items", defaultValue = "true")
	private Boolean unique;
}