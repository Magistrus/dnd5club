package club.dnd5.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ModeratorController {
	@GetMapping ("/profile/beast")
	public String getProfileForm(Model model) {
		return "user/admin/add_beast";
	}
}