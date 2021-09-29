package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.repository.datatable.ArmorDatatableRepository;


@Controller
public class ArmorController {
	@Autowired
	private ArmorDatatableRepository repository;

	@GetMapping("/armors")
	public String getArmors() {
		return "armors";
	}
	
	@GetMapping("/armors/{name}")
	public String getArmor(Model model, @PathVariable String name) {
		model.addAttribute("selectedArmor", "name");
		return "armors";
	}
	
	@GetMapping("/armors/fragment/{id}")
	public String getArmorFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("armor", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/armor :: view";
	}
}