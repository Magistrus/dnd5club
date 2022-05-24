package club.dnd5.portal.dto.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import club.dnd5.portal.dto.api.spells.Order;
import club.dnd5.portal.dto.api.spells.SearchRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
@NoArgsConstructor
public class RequestApi {
    public Integer page;
    public Integer limit = -1;
    public SearchRequest search;

    @JsonProperty("order")
    public List<Order> orders;
}