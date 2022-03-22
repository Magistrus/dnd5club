package club.dnd5.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import club.dnd5.portal.service.ArticleService;

@Controller
public class ProfileController {
	@Autowired
	private ArticleService service;
	
	@GetMapping ("/profile")
	public String getProfileForm(Model model) {
		return "user/profile";
	}
}