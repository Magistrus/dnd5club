package club.dnd5.portal.model.foundary;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum FDamageType {
	piercing("колющий", "колющего", "колющим"),
	slashing("дробящий", "дробяшего", "дробящим"),
	bludgeoning("рубящий", "рубящего", "рубящим"),

	thunder("звуком"),
	acid("кислотой", "кислотный"),
	radiant("лучистый", "излучением"),
	lightning("электричеством"),
	necrotic("некротический", "некротической энергией"),
	fire("урона огнем", "урон огнем"),
	psychic("психического", "психический", "психической"),
	force("силовым полем"),
	cold("холодом"),
	poison("урон ядом", "урона ядом"),
	
	healing("восстанавливает", "лечит");

	private static Pattern patternDamages;

	private Set<String> worlds;

	FDamageType(String... worlds){
		this.worlds = new HashSet<>(Arrays.asList(worlds));
	}
	
	public static Queue<String> parse(String text) {
		text = text.toLowerCase();
		if (patternDamages == null) {
			patternDamages = Pattern.compile(
					Arrays.stream(values()).flatMap(type -> type.worlds.stream()).collect(Collectors.joining("|")));
		}
		Matcher matcher = patternDamages.matcher(text);
		Queue<String> damages = new LinkedList<>();
		while (matcher.find()) {
			String group = matcher.group();
			for (FDamageType damageType : values()) {
				if (damageType.worlds.contains(group)) {
					damages.add(damageType.name());
					break;
				}
			}
		}
		return damages;
	}
}
