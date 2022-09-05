package club.dnd5.portal.controller.tools;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class TraderToolController {
	
	@GetMapping("/tools/trader")
	public String getTreasuryTool(Model model) {
		model.addAttribute("metaTitle", "Генератор лавки торговца");
		model.addAttribute("metaUrl", "https://dnd5.club/tools/trader");
		model.addAttribute("metaDescription", "Генерация содержимого лавки торговца");
		model.addAttribute("menuTitle", "Генератор содержимого лавки торговца");
		return "tools/traders";
	}
}