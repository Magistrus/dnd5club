package club.dnd5.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import club.dnd5.portal.repository.user.RoleRepository;

@Controller
public class AdminController {
	@Autowired
	private RoleRepository roleRepository;
	
	@GetMapping("/admin/users")
	public String getUsers(Model model) {
		model.addAttribute("roles", roleRepository.findAll());
		return "/user/admin/users";
	}
}