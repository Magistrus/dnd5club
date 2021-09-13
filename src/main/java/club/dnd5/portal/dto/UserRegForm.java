package club.dnd5.portal.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@PasswordMatches
public class UserRegForm {
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 30)
	private String name;

	@NotNull
	@NotEmpty
	@Size(min = 6, max = 128)
	private String password;

	@NotNull
	@NotEmpty
	@Size(min = 6, max = 128)
	private String passwordConfirm;

	@Email
	@NotEmpty
	private String email;
}