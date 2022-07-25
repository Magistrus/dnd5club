package club.dnd5.portal.controller;

import club.dnd5.portal.dto.user.UserDto;
import club.dnd5.portal.model.articles.ArtricleStatus;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.user.UserRepository;
import club.dnd5.portal.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class ProfileController {
	@Autowired
	private UserRepository usersRepository;

	@Autowired
	private ArticleService service;

	@GetMapping("/profile/{username}")
	public String getProfileForm(Model model, Authentication authentication, HttpServletRequest request) {
		model.addAttribute("moderate_article_count", service.getCountByStatus(ArtricleStatus.MODERATION));
		model.addAttribute("user_count", usersRepository.count());
		model.addAttribute("user_writer", usersRepository.countByRoles("WRITER"));
		model.addAttribute("user_moderator", usersRepository.countByRoles("MODERATOR"));

		return "user/profile";
	}

	@PostMapping(value = "/api/v1/profile/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProfile(@PathVariable String username) {
		Optional<User> user = usersRepository.findByName(username);

		if (!user.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		UserDto publicUser = new UserDto(user.get());

		return ResponseEntity.ok(publicUser);
	}
}
