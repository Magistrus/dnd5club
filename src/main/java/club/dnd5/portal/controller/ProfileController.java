package club.dnd5.portal.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import club.dnd5.portal.model.articles.ArtricleStatus;
import club.dnd5.portal.repository.user.UserRepository;
import club.dnd5.portal.service.ArticleService;

@Controller
public class ProfileController {
	@Autowired
	private UserRepository usersRepository;
	
	@Autowired
	private ArticleService service;
	
	@GetMapping ("/profile/{username}")
	public String getProfileForm(Model model, Authentication authentication, HttpServletRequest request) {
		model.addAttribute("moderate_article_count", service.getCountByStatus(ArtricleStatus.MODERATION));
		model.addAttribute("user_count", usersRepository.count()); 
		model.addAttribute("user_writer", usersRepository.countByRoles("WRITER"));
		model.addAttribute("user_moderator", usersRepository.countByRoles("MODERATOR"));

		return "user/profile";
	}
}