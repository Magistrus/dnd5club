package club.dnd5.portal.dto.api;

import java.util.List;
import java.util.stream.Collectors;

import club.dnd5.portal.model.user.Role;
import club.dnd5.portal.model.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserApi {
	private String username;
	private String name;
	private String email;
	private List<String> roles;
	
	public UserApi(User user) {
		username = user.getUsername();
		name = user.getName();
		email = user.getEmail();
		roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
	}
}
