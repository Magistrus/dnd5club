package club.dnd5.portal.model.foundary;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import club.dnd5.portal.model.creature.Action;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FAction {
    public String name;
    public List<Object> entries;
    public FAction(Action action) {
    	name = action.getName().replace("(перезарядка 6)", "{@recharge 6}").replace("(перезарядка 5-6)", "{@recharge 5}").replace("(перезарядка 4–6)", "{@recharge 4}");;
    	entries = Arrays.stream(action.getDescription().replace("<p>", "").split("</p>")).filter(t -> !t.isEmpty()).collect(Collectors.toList());
    }
}