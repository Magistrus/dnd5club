package club.dnd5.portal.controller.tools;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EncounterRandomController {
	@GetMapping("/tools/encounters")
	public String getTreasuryTool() {
		return "tools/random_encounters";
	}
}