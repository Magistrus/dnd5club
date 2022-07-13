package club.dnd5.portal.dto.user;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import club.dnd5.portal.model.user.Role;
import club.dnd5.portal.model.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OldUserDto {
	private static final String DATE_FORMAT = "d MMM yyyy HH:mm";
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
	
	private long id;
	private String name;
	private String email;
	private String createDate;
	private List<String> roles;
	public OldUserDto(User user) {
		id = user.getId();
		name = user.getName();
		email = user.getEmail();
		createDate = FORMATTER.format(user.getCreateDate());
		roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
	}
}