package club.dnd5.portal.dto.api.classes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.RequestApi;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class RaceRequestApi extends RequestApi {
	private RaceFilter filter;
}