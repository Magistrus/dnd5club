package club.dnd5.portal.model.fvtt.plutonium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import club.dnd5.portal.model.creature.Action;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FAction {
	private static final Pattern DAMAGE_FORMULA_PATTERN = Pattern.compile("<dice-roller formula=\"\\d+(к|d)\\d+(\\s\\+\\s\\d+){0,}\"/>");
	private static final Pattern DAMAGE_FORMULA = Pattern.compile("\\d+(к|d)\\d+(\\s\\+\\s\\d+){0,}");
    public String name;
    public List<Object> entries;
    public FAction(Action action) {
    	name = action.getName()
    			.replace("(перезарядка 6)", "{@recharge 6}")
    			.replace("(перезарядка 5-6)", "{@recharge 5}")
    			.replace("(перезарядка 4–6)", "{@recharge 4}");
    	entries = new ArrayList<>(5);
    	if (action.getDescription().contains("Рукопашная атака оружием")) {
    		Matcher matcher = Pattern.compile("\\d+").matcher(action.getDescription());
    		matcher.find();
    		int attack = Integer.valueOf(matcher.group());
    		entries.add(String.format("{@atk mw} {@hit %d} ", attack)); 
    	} 
    	if (action.getDescription().contains("Дальнобойная атака оружием")) {
    		Matcher matcher = Pattern.compile("\\d+").matcher(action.getDescription());
    		matcher.find();
    		int attack = Integer.valueOf(matcher.group());
    		entries.add(String.format("{@atk rw} {@hit %d} ", attack)); 
    	}
    	if (action.getDescription().contains("Рукопашная или дальнобойная атака оружием")) {
    		Matcher matcher = Pattern.compile("\\d+").matcher(action.getDescription());
    		matcher.find();
    		int attack = Integer.valueOf(matcher.group());
    		entries.add(String.format("{@atk mw,rw} {@hit %d} ", attack)); 
    	}
    	if (action.getDescription().contains("Рукопашная атака заклинанием")) {
    		Matcher matcher = Pattern.compile("\\d+").matcher(action.getDescription());
    		matcher.find();
    		int attack = Integer.valueOf(matcher.group());
    		entries.add(String.format("{@atk ms} {@hit %d} ", attack)); 
    	}
    	if (action.getDescription().contains("Дальнобойная атака заклинанием")) {
    		Matcher matcher = Pattern.compile("\\d+").matcher(action.getDescription());
    		if(matcher.find()) {
        		int attack = Integer.valueOf(matcher.group());
        		entries.add(String.format("{@atk rs} {@hit %d} ", attack)); 
    		}
    	}
    	
    	Matcher matcher = DAMAGE_FORMULA.matcher(action.getDescription());
    	String description = action.getDescription();
    	while (matcher.find()) {
    		String group = matcher.group();
    		String formula = "{@h}" + group.replace('к', 'd')
    				.replace("formula=\"", "{@damage ") + "}";
    		description = description.replace(group, formula);
    	}
    	description = description.replace("href=\"", "href=\"https://ttg.club/");
    	
    	matcher = DAMAGE_FORMULA_PATTERN.matcher(description);
    	while (matcher.find()) {
    		String group = matcher.group();
    		String formula = group.replace("<dice-roller formula=\"", "[[/r ").replace('к', 'd');
    		description = description.replace(group, formula);
    	}
    	entries.addAll(Arrays.stream(description.replace("<p>", "").split("</p>"))
    			.filter(t -> !t.isEmpty())
    			.collect(Collectors.toList()));
    	
    }
}