package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.repository.datatable.BackgroundDatatableRepository;

@Controller
public class BackgroundController {
	@Autowired
	private BackgroundDatatableRepository repository;

	@GetMapping("/backgrounds")
	public String getBackgrounds() {
		return "backgrounds";
	}
	
	@GetMapping("/backgrounds/{name}")
	public String getBackGround(Model model, @PathVariable String name) {
		model.addAttribute("selectedBackground", "name");
		return "backgrounds";
	}
	
	@GetMapping("/backgrounds/fragment/{id}")
	public String getBackgroundFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("background", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/background :: view";
	}
}