package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.model.items.Armor;
import club.dnd5.portal.repository.datatable.ArmorDatatableRepository;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class ArmorController {
	@Autowired
	private ArmorDatatableRepository repository;

	@GetMapping("/armors")
	public String getArmors(Model model) {
		model.addAttribute("metaTitle", "Доспехи (Armors) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/armors");
		model.addAttribute("metaDescription", "Доспехи по D&D 5 редакции");
		model.addAttribute("menuTitle", "Доспехи");
		return "armors";
	}
	
	@GetMapping("/armors/{name}")
	public String getArmor(Model model, @PathVariable String name, HttpServletRequest request) {
		Armor armor = repository.findByEnglishName(name.replace('_', ' '));
		if (armor == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("metaTitle", String.format("%s (%s) | Доспехи D&D 5e", armor.getName(), armor.getEnglishName()));
		model.addAttribute("metaUrl", "https://dnd5.club/armors/" + armor.getEnglishName().replace(" ", "_"));
		model.addAttribute("metaDescription", String.format("%s (%s) - доспехи по D&D 5 редакции", armor.getName(), armor.getEnglishName()));
		model.addAttribute("menuTitle", "Доспехи");
		return "armors";
	}
	
	@GetMapping("/armors/fragment/{id:\\d+}")
	public String getArmorFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("armor", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/armor :: view";
	}
	
	@GetMapping("/armors/fragment/{name:[A-Za-z_]+}")
	public String getMagicWeaponFragmentByName(Model model, @PathVariable String name) throws InvalidAttributesException {
		model.addAttribute("armor", repository.findByEnglishName(name.replace('_', ' ')));
		return "fragments/armor :: view";
	}
}