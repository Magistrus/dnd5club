package club.dnd5.portal.model.foundary;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import club.dnd5.portal.model.creature.CreatureFeat;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FTrait {
    public String name;
    public List<String> entries;
    public FTrait(CreatureFeat feat) {
    	name = feat.getName();
    	entries = Arrays.stream(feat.getDescription().replace("<p>", "").split("</p>")).filter(t -> !t.isEmpty()).collect(Collectors.toList());
    }
}