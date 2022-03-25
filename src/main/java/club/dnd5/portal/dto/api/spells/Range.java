package club.dnd5.portal.dto.api.spells;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.splells.Spell;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class Range {
	

	private String type;
    private Distance distance;
    public Range(Spell spell) {
		String distanceText = spell.getDistance();
		this.distance = new Distance();
		if(distanceText.contains("Касание")) {
			distance.setType("touch");
		}
		if(distanceText.contains("радиус")) {
			this.type = "radius";
		}
		else if (distanceText.contains("конус")) {
			this.type = "cone";
		}
		else if (distanceText.contains("линия")) {
			this.type = "line";
		}
		else if (distanceText.contains("полусфера")) {
			this.type = "hemisphere";
		}
		else if (distanceText.contains("куб")) {
			this.type = "cube";
		}
		else if (distanceText.contains("Особая")) {
			this.type = "special";
			distance = null;
		}
		else {
			this.type = "point";
		}
		Matcher matcher = Pattern.compile("\\d+").matcher(distanceText);
		if(matcher.find()) {
			distance.setAmount(Integer.valueOf(matcher.group()));
			if (distanceText.contains("футов")) {
				distance.setType("feet");
			} else if (distanceText.contains("миль") || distanceText.contains("мили")) {
				distance.setType("miles");
			} 
		} else if(distanceText.equals("На себя")) {
			distance.setType("self");
		} else if (distanceText.equals("Область видимости") || distanceText.equals("Видимость")) {
			distance.setType("sight");
		} else if (distanceText.equals("Без ограничений") || distanceText.equals("Неограниченная")) {
			distance.setType("unlimited");
		}
	}
}