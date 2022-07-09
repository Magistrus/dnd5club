package club.dnd5.portal.dto.api.classes;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import club.dnd5.portal.model.Dice;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class ClassFilter {
	@JsonProperty("book")
	private List<String> books;
	private Collection<Integer> hitdice;
}