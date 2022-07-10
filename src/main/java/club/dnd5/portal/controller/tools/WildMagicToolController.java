package club.dnd5.portal.controller.tools;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WildMagicToolController {

	@GetMapping("/tools/wildmagic")
	public String getTreasuryTool(Model model) {
		model.addAttribute("metaTitle", "Генератор эффектов дикой магии");
		model.addAttribute("metaUrl", "https://dnd5.club/tools/wildmagic");
		model.addAttribute("metaDescription", "Генерация эффектов дикой магии");
		model.addAttribute("menuTitle", "Генератор дикой магии");
		return "tools/wildmagic";
	}
}