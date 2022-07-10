package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.model.items.Weapon;
import club.dnd5.portal.repository.datatable.WeaponDatatableRepository;
import club.dnd5.portal.repository.datatable.WeaponPropertyDatatableRepository;


@Controller
public class WeaponController {
	@Autowired
	private WeaponDatatableRepository repository;
	
	@Autowired
	private WeaponPropertyDatatableRepository propertyRepository;
	
	@GetMapping("/weapons")
	public String getWeapons(Model model) {
		model.addAttribute("metaTitle", "Оружие (Weapons) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/weapons");
		model.addAttribute("metaDescription", "Оружие по D&D 5 редакции");
		model.addAttribute("menuTitle", "Оружие");
		return "weapons";
	}
	
	@GetMapping("/weapons/{name}")
	public String getWeapon(Model model, @PathVariable String name, HttpServletRequest request) {
		Weapon weapon = repository.findByEnglishName(name.replace('_', ' '));
		if (weapon == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("metaTitle", String.format("%s (%s) | D&D 5e", weapon.getName(), weapon.getEnglishName()));
		model.addAttribute("metaUrl", "https://dnd5.club/weapons/" + name);
		model.addAttribute("metaDescription", String.format("%s (%s) - %s D&D 5 редакции", weapon.getName(), weapon.getEnglishName(), weapon.getType().getName()));
		model.addAttribute("menuTitle", "Оружие");
		return "weapons";
	}
	
	@GetMapping("/weapons/fragment/{id:\\d+}")
	public String getMagicWeaponFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("weapon", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/weapon :: view";
	}
	
	@GetMapping("/weapons/fragment/{name:[A-Za-z_,']+}")
	public String getMagicWeaponFragmentByName(Model model, @PathVariable String name) throws InvalidAttributesException {
		model.addAttribute("weapon", repository.findByEnglishName(name.replace('_', ' ')));
		return "fragments/weapon :: view";
	}
	@GetMapping("/weapons/property/fragment/{name:[A-Za-z_,']+}")
	public String getWeaponFragmentPropertyByName(Model model, @PathVariable String name) throws InvalidAttributesException {
		model.addAttribute("property", propertyRepository.findByEnglishName(name.replace('_', ' ')));
		return "fragments/weapon :: property_view";
	}
}