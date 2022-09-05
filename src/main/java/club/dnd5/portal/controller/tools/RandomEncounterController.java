package club.dnd5.portal.controller.tools;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import club.dnd5.portal.model.creature.HabitatType;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class RandomEncounterController {
	@GetMapping("/tools/encounters")
	public String getView(Model model) {
		model.addAttribute("metaTitle", "Случайные столкновения");
		model.addAttribute("metaUrl", "https://dnd5.club/tools/encounters");
		model.addAttribute("metaDescription", "Генерация случайных столкновений");
		model.addAttribute("types", HabitatType.types());
		model.addAttribute("menuTitle", "Генератор случайных столкновений");
		return "tools/random_encounters";
	}
}