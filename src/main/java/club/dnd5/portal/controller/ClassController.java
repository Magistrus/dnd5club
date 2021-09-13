package club.dnd5.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import club.dnd5.portal.repository.classes.ClassRepository;

@Controller
public class ClassController {
	@Autowired
	private ClassRepository classRepository;

	@GetMapping("/classes")
	public String getClasses(Model model) {
		model.addAttribute("classes", classRepository.findAll());
		return "classes";
	}
}