package club.dnd5.portal.dto.api.spells;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private boolean ritual;
    private boolean concentration;
    @JsonProperty("damageType")
    private List<String> damageTypes;
    private boolean homebrew;
    private boolean settings;
}