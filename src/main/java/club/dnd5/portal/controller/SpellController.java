package club.dnd5.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpellController {
	@GetMapping("/spells")
	public String getSpells() {
		return "spells";
	}
}