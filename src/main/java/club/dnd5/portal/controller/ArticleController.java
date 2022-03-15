package club.dnd5.portal.controller;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.model.articles.AtricleStatus;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.user.UserRepository;
import club.dnd5.portal.service.ArticleService;

@Controller
public class ArticleController {
	@Autowired
	private ArticleService service;	
	
	@Autowired
	private UserRepository usersRepository;

	@GetMapping("/articles")
	public String getArticles(Model model) {
		model.addAttribute("articles", service.findAllByStatus(AtricleStatus.PUBLISHED));
		return "articles";
	}

	@GetMapping("/articles/{id}")
	public String getArticle(@PathVariable Integer id) {
		return "article";
	}
	
	@GetMapping("/profile/articles")
	public String getProfileArticles(Model model, Principal principal, HttpServletRequest request) {
		Optional<User> user = usersRepository.findByName(principal.getName());
		if (!user.isPresent()) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("articles", service.findAllByUser(user.get()));
		return "profile/articles";
	}
}