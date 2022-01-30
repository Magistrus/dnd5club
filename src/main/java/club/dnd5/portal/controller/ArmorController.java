package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.item.ArmorDto;
import club.dnd5.portal.model.items.Armor;
import club.dnd5.portal.repository.datatable.ArmorDatatableRepository;


@Controller
public class ArmorController {
	@Autowired
	private ArmorDatatableRepository repository;

	@GetMapping("/armors")
	public String getArmors(Model model) {
		model.addAttribute("metaTitle", "Доспехи (Armors) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/armors/");
		model.addAttribute("metaDescription", "Доспехи по D&D 5 редакции");
		return "armors";
	}
	
	@GetMapping("/armors/{name}")
	public String getArmor(Model model, @PathVariable String name) {
		Armor armor = repository.findByEnglishName(name.replace('_', ' '));
		model.addAttribute("selectedArmor", new ArmorDto(armor));
		model.addAttribute("metaTitle", String.format("%s (%s) | D&D 5e", armor.getName(), armor.getEnglishName()));
		model.addAttribute("metaUrl", "https://dnd5.club/armors/" + armor.getEnglishName().replace(" ", "_"));
		model.addAttribute("metaDescription", String.format("%s (%s) - доспехи по D&D 5 редакции", armor.getName(), armor.getEnglishName()));
		return "armors";
	}
	
	@GetMapping("/armors/fragment/{id}")
	public String getArmorFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("armor", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/armor :: view";
	}
}