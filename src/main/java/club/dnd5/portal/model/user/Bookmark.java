package club.dnd5.portal.model.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "bookmarks")
public class Bookmark {
	@Id
	private String uuid;
	private String name;
	private String url;
	
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "parent_id")
	private Bookmark parent;
	
	@OneToMany(mappedBy = "parent", orphanRemoval = true)
	private List<Bookmark> chields;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}