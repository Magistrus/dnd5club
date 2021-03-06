package club.dnd5.portal.model.tavern;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import club.dnd5.portal.model.creature.HabitatType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "taverna_dishes")
@Getter
@Setter
public class TavernaDish {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	@Enumerated(EnumType.STRING)
	private HabitatType habitat;
	@Enumerated(EnumType.STRING)
	private TavernaCategory category;
}