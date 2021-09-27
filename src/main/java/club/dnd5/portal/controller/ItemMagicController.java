package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.repository.datatable.MagicItemDatatableRepository;


@Controller
public class ItemMagicController {
	@Autowired
	private MagicItemDatatableRepository repository;

	@GetMapping("/items/magic")
	public String getMagicItems() {
		return "items_magic";
	}
	
	@GetMapping("/items/magic/{name}")
	public String getMagicItem(Model model, @PathVariable String name) {
		model.addAttribute("selectedmagicItem", "name");
		return "items_magic";
	}
	
	@GetMapping("/items/magic/fragment/{id}")
	public String getMagicItemFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("item", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/item_magic :: view";
	}
}