package club.dnd5.portal.controller.tools;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class MadnessToolController {
	@GetMapping("/tools/madness")
	public String getTreasuryTool(Model model) {
		model.addAttribute("metaTitle", "Генератор безумия");
		model.addAttribute("metaUrl", "https://ttg.club/tools/wildmagic");
		model.addAttribute("metaDescription", "Генерация кратковреммных, долговременных и бессрочных эффектов безумия");
		model.addAttribute("menuTitle", "Генератор безумия");
		return "tools/madness";
	}
}