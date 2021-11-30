package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.GodDto;
import club.dnd5.portal.repository.datatable.GodDatatableRepository;

@Controller
public class GodController {
	@Autowired
	private GodDatatableRepository repository;

	@GetMapping("/gods")
	public String getGods() {
		return "gods";
	}
	
	@GetMapping("/gods/{name}")
	public String getGod(Model model, @PathVariable String name) {
		model.addAttribute("selectedGod", new GodDto(repository.findByEnglishName(name.replace("_", " "))));
		return "gods";
	}
	
	@GetMapping("/gods/fragment/{id}")
	public String getGodFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("god", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/god :: view";
	}
}