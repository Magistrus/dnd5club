package club.dnd5.portal.controller;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import club.dnd5.portal.model.articles.ArtricleStatus;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.user.UserRepository;
import club.dnd5.portal.service.ArticleService;

@Controller
public class ProfileController {
	@Autowired
	private UserRepository usersRepository;
	
	@Autowired
	private ArticleService service;
	
	@Value("${git.commit.id}")
	private String version;
	
	@GetMapping ("/profile")
	public String getProfileForm(Model model, Principal principal, HttpServletRequest request) {
		Optional<User> user = usersRepository.findByName(principal.getName());
		if (!user.isPresent()) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		User currentUser = user.get();
		model.addAttribute("all_article_count", service.getCountArticlesByUser(user.get()));
		model.addAttribute("moderated_article_count", service.getCountByStatus(ArtricleStatus.MODERATION));
		model.addAttribute("name", currentUser.getName());
		model.addAttribute("email", currentUser.getEmail());
		model.addAttribute("roles", currentUser.getRoles());
		model.addAttribute("version", version);
		return "user/profile";
	}
}