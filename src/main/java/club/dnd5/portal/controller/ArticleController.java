package club.dnd5.portal.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import club.dnd5.portal.model.articles.Article;
import club.dnd5.portal.model.articles.ArtricleStatus;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.user.UserRepository;
import club.dnd5.portal.service.ArticleService;

@Controller
public class ArticleController {
	@Autowired
	private ArticleService service;	
	
	@Autowired
	private UserRepository usersRepository;
	
	@Value("${git.commit.id}")
	private String version;

	@GetMapping("/articles")
	public String getArticles(Model model) {
		model.addAttribute("articles", service.findAllByStatus(ArtricleStatus.PUBLISHED, Sort.by(Direction.DESC, "published")));
		model.addAttribute("version", version);
		return "articles";
	}

	@GetMapping("/articles/{id:\\d+}")
	public String getArticle(Model model, @PathVariable Integer id) {
		Optional<Article> article = service.findById(id);
		model.addAttribute("article", article.get());
		model.addAttribute("version", version);
		return "article";
	}
	
	@GetMapping("/profile/articles")
	public String getProfileArticles(Model model, String status, Principal principal, HttpServletRequest request) {
		Optional<User> user = usersRepository.findByName(principal.getName());
		if (!user.isPresent()) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("articles", service.findAllByCreator(user.get()));
		model.addAttribute("version", version);
		return "profile/articles";
	}
	
	@GetMapping("/profile/articles/form")
	public String getProfileArticleForm(Model model, Principal principal, HttpServletRequest request) {
		model.addAttribute("article", new Article());
		model.addAttribute("version", version);
		return "profile/form_article";
	}
	
	@PostMapping(value = "/profile/articles", params = "save")
	public String saveArticle(Model model, Principal principal, Article article) {
		Optional<User> creator = usersRepository.findByName(principal.getName());
		article = service.save(article, creator.get());
		model.addAttribute("article", article);
		model.addAttribute("version", version);
		return "profile/form_article";
	}
	
	@PostMapping(value = "/profile/articles", params = "preview")
	public String previewArticle(Model model, Principal principal, Article article) {
		model.addAttribute("article", article);
		model.addAttribute("version", version);
		return "article";
	}
	
	@PostMapping(value = "/profile/articles", params = "delete")
	public String deleteArticle(Model model, Principal principal, Article article) {
		Optional<User> creator = usersRepository.findByName(principal.getName());
		article.setStatus(ArtricleStatus.REMOVED);
		article.setDeleted(LocalDateTime.now());
		article = service.save(article, creator.get());
		model.addAttribute("version", version);
		return "redirect:/profile/articles";
	}
	
	@PostMapping(value = "/profile/articles", params = "moderate")
	public String moderateArticle(Model model, Principal principal, Article article) {
		Optional<User> user = usersRepository.findByName(principal.getName());
		article.setStatus(ArtricleStatus.MODERATION);
		article.setModerated(LocalDateTime.now());
		User currentUser = user.get();
		article = service.save(article, currentUser);
		model.addAttribute("version", version);
		return "redirect:/profile/articles";
	}
	
	@PostMapping(value = "/profile/articles", params = "publishe")
	public String publisheArticle(Model model, Principal principal, Article article) {
		Optional<User> user = usersRepository.findByName(principal.getName());
		article.setStatus(ArtricleStatus.PUBLISHED);
		article.setPublished(LocalDateTime.now());
		User currentUser = user.get();
		article = service.save(article, currentUser);
		model.addAttribute("version", version);
		return "redirect:/profile/articles";
	}

	@PostMapping(value = "/profile/articles", params = "cancel")
	public String cancelArticle(Model model, Principal principal, Article article) {
		model.addAttribute("version", version);
		return "redirect:/profile/articles";
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