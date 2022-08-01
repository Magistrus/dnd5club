package club.dnd5.portal.model.user;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "bookmarks")
public class Bookmark {
	@Id
	@Type(type = "uuid-char")
	private UUID uuid;
	private String name;
	private String url;
	private int order;
	
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "parent_id")
	private Bookmark parent;
	
	@OneToMany(mappedBy = "parent", orphanRemoval = true)
	private List<Bookmark> chields;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}