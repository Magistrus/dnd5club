package club.dnd5.portal.dto.api.tools;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class RequestTreasuryApi {
	private Integer cr;
	private Boolean coins;
	private Boolean magicItem;
	private Boolean scroll;
	private Boolean item;
	private Boolean trinket;
	private Boolean art;
	private Boolean gem;
	private Boolean unique;
}