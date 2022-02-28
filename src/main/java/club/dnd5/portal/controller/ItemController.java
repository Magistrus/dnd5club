package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.item.ItemDto;
import club.dnd5.portal.model.items.Equipment;
import club.dnd5.portal.model.items.EquipmentType;
import club.dnd5.portal.repository.datatable.ItemDatatableRepository;

@Controller
public class ItemController {
	@Autowired
	private ItemDatatableRepository repository;

	@GetMapping("/items")
	public String getItems(Model model) {
		model.addAttribute("categories", EquipmentType.values());
		model.addAttribute("metaTitle", "Снаряжение (Items) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/items");
		model.addAttribute("metaDescription", "Снаряжение и инструменты по D&D 5 редакции");
		return "items";
	}
	
	@GetMapping("/items/{name}")
	public String getItem(Model model, @PathVariable String name, HttpServletRequest request) {
		Equipment item = repository.findByEnglishName(name.replace("_", " "));
		if (item == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("categories", EquipmentType.values());
		
		model.addAttribute("selectedItem", new ItemDto(item));
		model.addAttribute("metaTitle", item.getName() + " | Снаряжение D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/items/" + name);
		model.addAttribute("metaDescription", String.format("%s (%s) снаряжение по D&D 5 редакции", item.getName(), item.getEnglishName()));
		return "items";
	}
	
	@GetMapping("/items/fragment/{id}")
	public String getMagicItemFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("item", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/item :: view";
	}
}