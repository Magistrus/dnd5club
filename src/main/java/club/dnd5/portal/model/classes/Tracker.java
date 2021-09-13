package club.dnd5.portal.model.classes;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import club.dnd5.portal.model.Rest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "trackers")
public class Tracker {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String value;

	@Enumerated(EnumType.STRING)
	private Type type;
	@Enumerated(EnumType.STRING)
	private Rest rest;
	
	enum Type {
		Number,
		Counter,
		Roll
	}
}