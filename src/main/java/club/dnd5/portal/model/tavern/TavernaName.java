package club.dnd5.portal.model.tavern;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import club.dnd5.portal.model.races.Sex;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "taverna_names")
@Getter
@Setter
public class TavernaName {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String names;
	@Enumerated(EnumType.STRING)
	private Sex sex;
	@Enumerated(EnumType.STRING)
	private ObjectType objectType;

	public static enum ObjectType{
		ANIMATED,
		OBJECT
	}
}