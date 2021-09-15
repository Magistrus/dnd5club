package club.dnd5.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreatureController {
	@GetMapping("/creatures")
	public String getCreatures() {
		return "bestiary";
	}
}
