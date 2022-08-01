package club.dnd5.portal.dto.api.spells;

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
public class Order {
	@Schema(description = "The name field for order", defaultValue = "name")
    private String field;
	@Schema(description = "The order direction", defaultValue = "asc")
    private String direction;
}