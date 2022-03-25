package club.dnd5.portal.dto.api.spells;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpellRequestDto {
    public Integer page;
    public Integer limit = Integer.MAX_VALUE;
    public String search;
    public SpellFilter filter;

    @JsonProperty("order")
    public List<Order> orders;
}
