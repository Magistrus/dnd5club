package club.dnd5.portal.model.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import club.dnd5.portal.dto.UserRegForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String username;
	private String password;
	private String email;
	private LocalDateTime createDate;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

    @Column(name = "enabled")
    private boolean enabled;

    public User() {
    	this.createDate = LocalDateTime.now();
    }

	public User(UserRegForm userForm) {
		this.name = userForm.getName();
		this.password = userForm.getPassword();
		this.email = userForm.getEmail();
		this.createDate = LocalDateTime.now();
	}
}