package club.dnd5.portal.controller.api.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.UserApi;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.user.UserRepository;
import club.dnd5.portal.service.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "The User API")
@RestController
@RequestMapping("/api/v1/user")
public class UserApiController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/status")
	public ResponseEntity<?> getStatus() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<User> user = userRepository.findByEmailOrUsername(authentication.getName(), authentication.getName());
			if (user.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK).build();
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@GetMapping("/info")
	public ResponseEntity<UserApi> getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    Optional<User> user = userRepository.findByEmailOrUsername(authentication.getName(), authentication.getName());
		    if (user.isPresent()) {
		    	return ResponseEntity.ok(new UserApi(user.get()));
		    }
		    return ResponseEntity.notFound().build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
