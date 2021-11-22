package club.dnd5.portal.controller.tools;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemMagicBuyingController {
	@GetMapping("/tools/treasury")
	public String getTreasuryTool() {
		return "tools/treasury";
	}
}