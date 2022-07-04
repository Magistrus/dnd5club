package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.item.TreasureDto;
import club.dnd5.portal.model.items.TreasureType;
import club.dnd5.portal.repository.datatable.TreasureDatatableRepository;

@Controller
public class TreasureController {
	@Autowired
	private TreasureDatatableRepository repository;

	@GetMapping("/treasures")
	public String getItems(Model model) {
		model.addAttribute("categories", TreasureType.values());
		model.addAttribute("metaTitle", "Драгоценности и безделушки (Treasures) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/treasures");
		model.addAttribute("metaDescription", "Драгоценности и безделушки по D&D 5 редакции");
		model.addAttribute("menuTitle", "Драгоценности и безделушки");
		return "treasures";
	}
	
	@GetMapping("/treasures/{name}")
	public String getItem(Model model, @PathVariable String name) {
		model.addAttribute("categories", TreasureType.values());
		model.addAttribute("selectedTreasure", new TreasureDto(repository.findByEnglishName(name.replace("_", " "))));
		return "treasures";
	}
	
	@GetMapping("/treasures/fragment/{id}")
	public String getMagicItemFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("item", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/treasure :: view";
	}
}