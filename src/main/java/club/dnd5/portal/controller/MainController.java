package club.dnd5.portal.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@Value("${git.commit.id}")
	private String version;

	@GetMapping("/")
	public String getHome(Model model) {
		model.addAttribute("version", version);
		return "index";
	}
	
	@GetMapping("/search")
	public String getSearch() {
		return "search";
	}
}