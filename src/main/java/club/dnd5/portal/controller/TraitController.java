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

import club.dnd5.portal.dto.trait.TraitDto;
import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.SkillType;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.trait.Trait;
import club.dnd5.portal.repository.datatable.TraitDatatableRepository;

@Controller
public class TraitController {
	@Autowired
	private TraitDatatableRepository repository;
	
	private Map<TypeBook, List<Book>> sources;

	@PostConstruct
	public void init() {
		sources = new HashMap<>();
		sources.put(TypeBook.OFFICAL, repository.findBook(TypeBook.OFFICAL));
		sources.put(TypeBook.SETTING, repository.findBook(TypeBook.SETTING));
		sources.put(TypeBook.CUSTOM, repository.findBook(TypeBook.CUSTOM));
	}
	
	@GetMapping("/traits")
	public String getTraits(Model model) {
		model.addAttribute("books", sources.get(TypeBook.OFFICAL));
		model.addAttribute("settingBooks", sources.get(TypeBook.SETTING));
		model.addAttribute("hombrewBooks", sources.get(TypeBook.CUSTOM));
		model.addAttribute("abilities", AbilityType.getBaseAbility());
		model.addAttribute("skills", SkillType.values());
		model.addAttribute("prerequisites", repository.findAllPrerequisite());
		model.addAttribute("metaTitle", "Черты (Traits) D&D 5e");
		model.addAttribute("menuTitle", "Черты");
		model.addAttribute("metaUrl", "https://dnd5.club/traits");
		model.addAttribute("metaDescription", "Списко черт персонажей по D&D 5 редакции");
		return "traits";
	}
	
	@GetMapping("/traits/{name}")
	public String getTrait(Model model, @PathVariable String name, HttpServletRequest request) {
		Trait trait = repository.findByEnglishName(name.replace("_", " "));
		if (trait == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("books", sources.get(TypeBook.OFFICAL));
		model.addAttribute("settingBooks", sources.get(TypeBook.SETTING));
		model.addAttribute("moduleBooks", sources.get(TypeBook.MODULE));
		model.addAttribute("hombrewBooks", sources.get(TypeBook.CUSTOM));
		model.addAttribute("abilities", AbilityType.getBaseAbility());
		model.addAttribute("skills", SkillType.values());
		model.addAttribute("prerequisites", repository.findAllPrerequisite());

		model.addAttribute("selectedTrait", new TraitDto(trait));
		model.addAttribute("metaTitle", String.format("%s (%s)", trait.getName(), trait.getEnglishName()) + " | Черты D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/traits/" + name);
		model.addAttribute("metaDescription", String.format("%s (%s) - черта персонажа по D&D 5-редакции", trait.getName(), trait.getEnglishName()));
		model.addAttribute("menuTitle", "Черты");
		return "traits";
	}
	
	@GetMapping("/traits/fragment/{id}")
	public String getTraitFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("trait", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/trait :: view";
	}
}