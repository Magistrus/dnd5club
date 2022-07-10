package club.dnd5.portal.dto.api.wiki;

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
public class GodFilter {
	private List<String> alignment;
	private List<String> domain;
	private List<String> rank;
	private List<Integer> pantheon;
	@JsonProperty("book")
	private List<String> books;
}