package club.dnd5.portal.controller.tools;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class TreasuryToolController {
	
	@GetMapping("/tools/treasury")
	public String getTreasuryTool(Model model) {
		model.addAttribute("metaTitle", "Генератор сокровищницы");
		model.addAttribute("metaUrl", "https://dnd5.club/tools/treasury");
		model.addAttribute("metaDescription", "Генерация содержимого сокровищницы");
		model.addAttribute("menuTitle", "Генератор содержимого сокровищницы");

		return "tools/treasury";
	}
}