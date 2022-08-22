package club.dnd5.portal.model.creature;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.StringUtils;

import club.dnd5.portal.model.SkillType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "creature_skills")
public class Skill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private SkillType type;
	
	private byte bonus;

	public Skill(SkillType type, byte bonus) {
		this.type = type;
		this.bonus = bonus;
	}

	public String getText() {
		return type != null ? String.format("%s %+d", StringUtils.capitalize(type.name().toLowerCase().replace('_', ' ')), bonus) : "";
	}

	public String getCyrilicText() {
		return type != null
				? String.format("%s %+d", StringUtils.capitalize(type.getCyrilicName().toLowerCase()), bonus)
				: "";
	}
}