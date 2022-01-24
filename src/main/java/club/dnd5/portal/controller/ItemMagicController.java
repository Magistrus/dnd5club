package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.item.ItemMagicDto;
import club.dnd5.portal.model.items.MagicItem;
import club.dnd5.portal.model.items.MagicThingType;
import club.dnd5.portal.model.items.Rarity;
import club.dnd5.portal.repository.datatable.MagicItemDatatableRepository;


@Controller
public class ItemMagicController {
	@Autowired
	private MagicItemDatatableRepository repository;

	@GetMapping("/items/magic")
	public String getMagicItems(Model model) {
		model.addAttribute("rarities", Rarity.values());
		model.addAttribute("types", MagicThingType.values());
		model.addAttribute("metaTitle", "Магические предметы (Magic items) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/items/magic/");
		model.addAttribute("metaDescription", "Магические предметы и артефакты по D&D 5 редакции");
		return "items_magic";
	}
	
	@GetMapping("/items/magic/{name}")
	public String getMagicItem(Model model, @PathVariable String name) {
		model.addAttribute("rarities", Rarity.values());
		model.addAttribute("types", MagicThingType.values());
		MagicItem item = repository.findByEnglishName(name.replace("_", " "));
		model.addAttribute("selectedItemMagic", new ItemMagicDto(item));
		model.addAttribute("metaTitle", item.getName() + " Магические предметы D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/items/magic/" + name);
		model.addAttribute("metaDescription", String.format("%s (%s) - %s %s", item.getName(), item.getEnglishName(), item.getRarity().getCyrilicName(), item.getType().getCyrilicName()));
		return "items_magic";
	}
	
	@GetMapping("/items/magic/fragment/{id}")
	public String getMagicItemFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("item", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/item_magic :: view";
	}
}