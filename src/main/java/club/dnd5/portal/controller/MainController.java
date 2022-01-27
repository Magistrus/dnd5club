package club.dnd5.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping("/")
	public String getHome() {
		return "index";
	}
	
	@GetMapping("/search")
	public String getSearch() {
		return "search";
	}
}