package club.dnd5.portal.dto.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import club.dnd5.portal.dto.api.spells.Order;
import club.dnd5.portal.dto.api.spells.SearchRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
@NoArgsConstructor
public class RequestApi {
	@Schema(description = "spell page", defaultValue = "0")
    public Integer page;
	@Schema(description = "spells limit on one page", defaultValue = "10")
    public Integer limit = -1;
	@Schema(description = "the serach object", defaultValue = "null")
    public SearchRequest search;

    @JsonProperty("order")
    public List<Order> orders;
}