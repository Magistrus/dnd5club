package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.item.WeaponDto;
import club.dnd5.portal.model.DamageType;
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
		model.addAttribute("damageTypes", DamageType.getWeaponDamage());
		model.addAttribute("properties", propertyRepository.findAll());
		model.addAttribute("metaTitle", "Оружие [Weapons] D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/weapons/");
		model.addAttribute("metaDescription", "Оружие по D&D 5 редакции");
		return "weapons";
	}
	
	@GetMapping("/weapons/{name}")
	public String getWeapon(Model model, @PathVariable String name) {
		model.addAttribute("damageTypes", DamageType.getWeaponDamage());
		model.addAttribute("properties", propertyRepository.findAll());
		Weapon weapon = repository.findByEnglishName(name.replace('_', ' '));
		model.addAttribute("selectedWeapon", new WeaponDto(weapon));
		model.addAttribute("metaTitle", weapon.getName() + " D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/weapons/" + name);
		model.addAttribute("metaDescription", "Оружие по D&D 5 редакции");
		return "weapons";
	}
	
	@GetMapping("/weapons/fragment/{id}")
	public String getMagicWeaponFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("weapon", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/weapon :: view";
	}
}