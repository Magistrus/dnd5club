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
public class SpellFilter {
	@JsonProperty("book")
	private List<String> books;
	@JsonProperty("level")
	private List<Integer> levels;
    
    @JsonProperty("class") 
    private List<String> myclass;
    @JsonProperty("school") 
    private List<String> schools;
    @JsonProperty("component") 
    private List<String> components;
    private Boolean ritual;
    private Boolean concentration;
    @JsonProperty("damageType")
    private List<String> damageTypes;
    private Boolean homebrew;
    private Boolean settings;
}