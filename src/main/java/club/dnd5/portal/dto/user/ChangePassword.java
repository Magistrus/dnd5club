package club.dnd5.portal.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassword {
	private String token;
	private String password;
}
