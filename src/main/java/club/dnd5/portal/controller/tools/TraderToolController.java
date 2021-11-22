package club.dnd5.portal.controller.tools;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TraderToolController {
	@GetMapping("/tools/trader")
	public String getTreasuryTool() {
		return "tools/trader";
	}
}