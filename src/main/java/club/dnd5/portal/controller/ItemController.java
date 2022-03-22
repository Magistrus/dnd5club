package club.dnd5.portal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.naming.directory.InvalidAttributesException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.item.ItemDto;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.items.Equipment;
import club.dnd5.portal.model.items.EquipmentType;
import club.dnd5.portal.repository.datatable.ItemDatatableRepository;

@Controller
public class ItemController {
	@Autowired
	private ItemDatatableRepository repository;
	
	private Map<TypeBook, List<Book>> sources;

	@Value("${git.commit.id}")
	private String version;

	@PostConstruct
	public void init() {
		sources = new HashMap<>();
		sources.put(TypeBook.OFFICAL, repository.findBook(TypeBook.OFFICAL));
		sources.put(TypeBook.SETTING, repository.findBook(TypeBook.SETTING));
		sources.put(TypeBook.MODULE, repository.findBook(TypeBook.MODULE));
		sources.put(TypeBook.CUSTOM, repository.findBook(TypeBook.CUSTOM));
	}
	
	@GetMapping("/items")
	public String getItems(Model model) {
		model.addAttribute("books", sources.get(TypeBook.OFFICAL));
		model.addAttribute("settingBooks", sources.get(TypeBook.SETTING));
		model.addAttribute("moduleBooks", sources.get(TypeBook.MODULE));
		model.addAttribute("hombrewBooks", sources.get(TypeBook.CUSTOM));
		model.addAttribute("categories", EquipmentType.values());
		model.addAttribute("metaTitle", "Снаряжение (Items) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/items");
		model.addAttribute("metaDescription", "Снаряжение и инструменты по D&D 5 редакции");
		model.addAttribute("version", version);
		return "items";
	}
	
	@GetMapping("/items/{name}")
	public String getItem(Model model, @PathVariable String name, HttpServletRequest request) {
		Equipment item = repository.findByEnglishName(name.replace("_", " "));
		if (item == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("books", sources.get(TypeBook.OFFICAL));
		model.addAttribute("settingBooks", sources.get(TypeBook.SETTING));
		model.addAttribute("moduleBooks", sources.get(TypeBook.MODULE));
		model.addAttribute("hombrewBooks", sources.get(TypeBook.CUSTOM));
		
		model.addAttribute("categories", EquipmentType.values());
		model.addAttribute("selectedItem", new ItemDto(item));
		model.addAttribute("metaTitle", item.getName() + " | Снаряжение D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/items/" + name);
		model.addAttribute("metaDescription", String.format("%s (%s) снаряжение по D&D 5 редакции", item.getName(), item.getEnglishName()));
		model.addAttribute("version", version);
		return "items";
	}
	
	@GetMapping("/items/fragment/{id:\\d+}")
	public String getMagicItemFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("item", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/item :: view";
	}
	
	@GetMapping("/items/fragment/{name:[A-Za-z_,']+}")
	public String getMagicWeaponFragmentByName(Model model, @PathVariable String name) throws InvalidAttributesException {
		model.addAttribute("item", repository.findByEnglishName(name.replace('_', ' ')));
		return "fragments/item :: view";
	}
}