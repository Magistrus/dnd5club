package club.dnd5.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserProfileController {
	@GetMapping ("/profile")
	public String getProfileForm(Model model) {
		return "user/profile";
	}
}