package club.dnd5.portal.controller.tools;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AbilityCalculatorController {
	@GetMapping("/tools/ability-calc")
	public String getAbilityCalculator() {
		return "/tools/ability-calc";
	}
}