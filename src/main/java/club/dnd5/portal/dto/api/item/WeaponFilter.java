package club.dnd5.portal.dto.api.item;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class WeaponFilter {
	List<String> properrty;
	List<String> damageType;
	List<String> dice;
	
	@JsonProperty("book")
	private List<String> books;
}