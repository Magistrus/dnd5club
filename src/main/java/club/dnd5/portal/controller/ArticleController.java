package club.dnd5.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ArticleController {
	
	@GetMapping("/articles")
	public String getArticles() {
		return "articles";
	}

	@GetMapping("/articles/form")
	public String getFormArticle() {
		return "form_article";
	}
	
	@GetMapping("/articles/{id}")
	public String getArticle(@PathVariable Integer id) {
		return "article";
	}
}