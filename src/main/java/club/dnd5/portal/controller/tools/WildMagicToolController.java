package club.dnd5.portal.controller.tools;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import club.dnd5.portal.model.splells.WildMagic;
import club.dnd5.portal.repository.datatable.WildMagicRepository;

@Controller
public class WildMagicToolController {
	public static final Random rnd = new Random();
	
	@Autowired
	private WildMagicRepository repo;
	
	@GetMapping("/tools/wildmagic")
	public String getTreasuryTool(Model model) {
		model.addAttribute("metaTitle", "Генератор дикой магии");
		model.addAttribute("metaUrl", "https://dnd5.club/tools/wildmagic");
		model.addAttribute("metaDescription", "Генерация эффектов дикой магии");
		return "tools/wildmagic";
	}
	
	@GetMapping("/tools/wildmagic/random")
	@ResponseBody
	public String getWildMagicRandomText(String type) {
		List<WildMagic> magics = null;
		if ("base".equals(type)) {
			magics = repo.findAll().subList(0, 50);	
		}
		else {
			magics = repo.findAll();
		}
		if (!magics.isEmpty()) {
			return magics.get(rnd.nextInt(magics.size())).getDescription();
		}
		return "Нет дикой магии сегодня";
	}
}