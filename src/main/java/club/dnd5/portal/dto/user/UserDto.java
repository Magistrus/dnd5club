package club.dnd5.portal.dto.user;

import club.dnd5.portal.model.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class UserDto {
	private String username;
	private String email;

	public UserDto(User user) {
		this.email = user.getEmail();
		this.username = user.getUsername();
	}
}
