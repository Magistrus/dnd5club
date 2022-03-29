package club.dnd5.portal.dto.api.spells;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class Duration {
	public String type;
	private String raw;
	private DurationUnit duration;
	private Boolean concentration;
	private List<String> ends;

	public Duration(String duration) {
		this.raw = duration;
	    if (duration.equals("Мгновенная")) {
	    	type = "instant";
	    } else if (duration.toLowerCase().equals("пока не рассеется") || duration.equals("До развеивания") || duration.equals("До рассеивания")) {
	    	type = "permanent";
	    	ends = Arrays.asList("dispel");
	    }
	    if (duration.contains(" не сработает")) {
	    	ends = Arrays.asList("trigger");
	    }
	    Matcher matcher = Pattern.compile("\\d+").matcher(duration);
		if(matcher.find()) {
	    	type = "timed";
	    	this.duration = new DurationUnit();
	    	this.duration.setAmount(Byte.valueOf(matcher.group()));
	    	if (duration.contains("час")) {
	    		this.duration.setType("hours");
	    	} else if (duration.contains("минут")){
	    		this.duration.setType("minutes");
	    	} else if (duration.contains("раунд") || duration.contains("ход")){
	    		this.duration.setType("rounds");
	    	} else if (duration.contains("дня") || duration.contains("дней") || duration.contains("день")){
	    		this.duration.setType("days");
	    	} else if (duration.contains("год")){
	    		this.duration.setType("years");
	    	}
		    if (duration.contains("до ")) {
		    	this.duration.setUpTo(Boolean.TRUE);
		    }
		}
		if (duration.contains("Концентрация")) {
			concentration = Boolean.TRUE;
		}
	}
}
