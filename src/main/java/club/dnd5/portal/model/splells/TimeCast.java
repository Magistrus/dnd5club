package club.dnd5.portal.model.splells;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import club.dnd5.portal.model.TimeUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@Entity
@NoArgsConstructor
@Table(name ="spells_times")
public class TimeCast {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private int number;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TimeUnit unit;
	private String condition;
	
	public TimeCast(int number, TimeUnit unit) {
		this.number = number;
		this.unit = unit;
	}

	public String getName(){
		if (condition == null) {
			return String.format("%d %s", number, unit.getName(number));
		}
		return String.format("%d %s, %s ", number, unit.getName(number), condition);
	}
}