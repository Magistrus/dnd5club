package club.dnd5.portal.dto.api.spells;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.splells.TimeCast;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class Timecast {
    public int number;
    public String unit;
    public String condition;
    public Timecast(TimeCast time) {
    	this.number = time.getNumber();
    	this.unit = time.getUnit().name().toLowerCase();
    	if (time.getCondition() != null) {
    		this.condition = time.getCondition();
    	}
	}
}
