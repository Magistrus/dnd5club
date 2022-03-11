package club.dnd5.portal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.naming.directory.InvalidAttributesException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.item.WeaponDto;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.items.Weapon;
import club.dnd5.portal.repository.datatable.WeaponDatatableRepository;
import club.dnd5.portal.repository.datatable.WeaponPropertyDatatableRepository;


@Controller
public class WeaponController {
	@Autowired
	private WeaponDatatableRepository repository;
	
	@Autowired
	private WeaponPropertyDatatableRepository propertyRepository;
	
	private Map<TypeBook, List<Book>> sources;

	@PostConstruct
	public void init() {
		sources = new HashMap<>();
		sources.put(TypeBook.OFFICAL, repository.findBook(TypeBook.OFFICAL));
		sources.put(TypeBook.SETTING, repository.findBook(TypeBook.SETTING));
		sources.put(TypeBook.MODULE, repository.findBook(TypeBook.MODULE));
		sources.put(TypeBook.CUSTOM, repository.findBook(TypeBook.CUSTOM));
	}
	
	@GetMapping("/weapons")
	public String getWeapons(Model model) {
		model.addAttribute("books", sources.get(TypeBook.OFFICAL));
		model.addAttribute("settingBooks", sources.get(TypeBook.SETTING));
		model.addAttribute("moduleBooks", sources.get(TypeBook.MODULE));
		model.addAttribute("hombrewBooks", sources.get(TypeBook.CUSTOM));
		model.addAttribute("damageTypes", DamageType.getWeaponDamage());
		model.addAttribute("properties", propertyRepository.findAll());
		model.addAttribute("metaTitle", "Оружие (Weapons) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/weapons");
		model.addAttribute("metaDescription", "Оружие по D&D 5 редакции");
		return "weapons";
	}
	
	@GetMapping("/weapons/{name}")
	public String getWeapon(Model model, @PathVariable String name, HttpServletRequest request) {
		Weapon weapon = repository.findByEnglishName(name.replace('_', ' '));
		if (weapon == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("books", sources.get(TypeBook.OFFICAL));
		model.addAttribute("settingBooks", sources.get(TypeBook.SETTING));
		model.addAttribute("moduleBooks", sources.get(TypeBook.MODULE));
		model.addAttribute("hombrewBooks", sources.get(TypeBook.CUSTOM));
		model.addAttribute("damageTypes", DamageType.getWeaponDamage());
		model.addAttribute("properties", propertyRepository.findAll());

		model.addAttribute("selectedWeapon", new WeaponDto(weapon));
		model.addAttribute("metaTitle", String.format("%s (%s) | D&D 5e", weapon.getName(), weapon.getEnglishName()));
		model.addAttribute("metaUrl", "https://dnd5.club/weapons/" + name);
		model.addAttribute("metaDescription", String.format("%s (%s) - %s D&D 5 редакции", weapon.getName(), weapon.getEnglishName(), weapon.getType().getName()));
		return "weapons";
	}
	
	@GetMapping("/weapons/fragment/{id}")
	public String getMagicWeaponFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("weapon", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/weapon :: view";
	}
}