package club.dnd5.portal.model.tavern;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import club.dnd5.portal.model.races.Sex;
import club.dnd5.portal.model.tavern.TavernaName.ObjectType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "taverna_prefixes")
@Getter
@Setter
public class TavernaPrefixName {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String male;
	private String female;
	private String neuter;

	@Enumerated(EnumType.STRING)
	private ObjectType objectType;

	public String getName(Sex sex) {
		switch (sex) {
		case MALE:
			return name + male;
		case FEMALE:
			return name + female;
		default:
			return name + neuter;
		}
	}
}