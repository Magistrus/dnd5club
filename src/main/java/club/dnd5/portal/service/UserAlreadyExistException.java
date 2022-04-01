package club.dnd5.portal.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAlreadyExistException extends Exception {
	private static final long serialVersionUID = -1675144205009540967L;
	private String message;
}