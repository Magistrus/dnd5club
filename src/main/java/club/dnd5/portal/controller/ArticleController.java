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
import org.springframework.web.bind.annotation.PostMapping;

import club.dnd5.portal.model.articles.Article;
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

	@GetMapping("/articles/{id:\\d+}")
	public String getArticle(Model model, @PathVariable Integer id) {
		Optional<Article> article = service.findById(id);
		model.addAttribute("article", article.get());
		return "article";
	}
	
	@GetMapping("/profile/articles")
	public String getProfileArticles(Model model, Principal principal, HttpServletRequest request) {
		Optional<User> user = usersRepository.findByName(principal.getName());
		if (!user.isPresent()) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("articles", service.findAllByCreator(user.get()));
		return "profile/articles";
	}
	
	@GetMapping("/profile/articles/form")
	public String getProfileArticleForm(Model model, Principal principal, HttpServletRequest request) {
		model.addAttribute("article", new Article());
		return "profile/form_article";
	}
	
	@PostMapping("/profile/articles")
	public String saveArticle(Model model, Principal principal, Article article) {
		Optional<User> creator = usersRepository.findByName(principal.getName());
		article = service.save(article, creator.get());
		model.addAttribute("article", article);
		return "profile/form_article";
	}
	
	@GetMapping("/profile/articles/{id:\\d+}")
	public String editArticle(Model model, @PathVariable Integer id, HttpServletRequest request) {
		Optional<Article> article = service.findById(id);
		if (!article.isPresent()) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("article", article.get());
		return "profile/form_article";
	}
}