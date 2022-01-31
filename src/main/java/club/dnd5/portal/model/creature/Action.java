package club.dnd5.portal.model.creature;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "creature_actions")
public class Action {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	//private String englishName;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Enumerated(EnumType.ORDINAL)
	private ActionType actionType;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "action_id")
	private Collection<ActionData> actionData;

	private String img;
}