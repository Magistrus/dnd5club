package club.dnd5.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping("/")
	public String getHome(Model model) {
		return "index";
	}
	
	@GetMapping("/search")
	public String getSearch(Model model) {
		return "search";
	}
	
	@GetMapping("/page")
	public String getPageText(Model model) {
		return "page";
	}
	
	@GetMapping("/telegram_bot")
	public String getPageTelegrammBot(Model model) {
		return "telegram_bot";
	}
}