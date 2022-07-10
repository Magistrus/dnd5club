package club.dnd5.portal.controller;

import java.util.Collection;

import javax.naming.directory.InvalidAttributesException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.model.items.MagicItem;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.datatable.MagicItemDatatableRepository;


@Controller
public class MagicItemController {
	@Autowired
	private MagicItemDatatableRepository repository;
	@Autowired
	private ImageRepository imageRepo;
	
	@GetMapping("/items/magic")
	public String getMagicItems(Model model) {
		model.addAttribute("metaTitle", "Магические предметы (Magic items) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/items/magic");
		model.addAttribute("metaDescription", "Магические предметы и артефакты по D&D 5 редакции");
		model.addAttribute("menuTitle", "Магические предметы");
		return "items_magic";
	}
	
	@GetMapping("/items/magic/{name}")
	public String getMagicItem(Model model, @PathVariable String name, HttpServletRequest request) {
		MagicItem item = repository.findByEnglishName(name.replace("_", " "));
		if (item == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("metaTitle", item.getName() + " | Магические предметы D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/items/magic/" + name);
		model.addAttribute("metaDescription", String.format("%s (%s) - %s %s", item.getName(), item.getEnglishName(), item.getRarity().getCyrilicName(), item.getType().getCyrilicName()));
		Collection<String> images = imageRepo.findAllByTypeAndRefId(ImageType.MAGIC_ITEM, item.getId());
		if (!images.isEmpty()) {
			model.addAttribute("metaImage", images.iterator().next());
		}
		model.addAttribute("menuTitle", "Магические предметы");
		return "items_magic";
	}
	
	@GetMapping("/items/magic/fragment/{id:\\d+}")
	public String getMagicItemFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		MagicItem item = repository.findById(id).orElseThrow(InvalidAttributesException::new);
		model.addAttribute("item", item);
		Collection<String> images = imageRepo.findAllByTypeAndRefId(ImageType.MAGIC_ITEM, item.getId());
		model.addAttribute("images", images);
		return "fragments/item_magic :: view";
	}
	
	@GetMapping("/items/magic/fragment/{name:[A-Za-z_,']+}")
	public String getMagicWeaponFragmentByName(Model model, @PathVariable String name) {
		model.addAttribute("item", repository.findByEnglishName(name.replace('_', ' ')));
		return "fragments/item_magic :: view";
	}
}