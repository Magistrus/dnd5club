package club.dnd5.portal.controller;

import java.util.Collection;
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

import club.dnd5.portal.dto.item.ItemMagicDto;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.model.items.MagicItem;
import club.dnd5.portal.model.items.MagicThingType;
import club.dnd5.portal.model.items.Rarity;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.datatable.MagicItemDatatableRepository;


@Controller
public class ItemMagicController {
	@Autowired
	private MagicItemDatatableRepository repository;
	@Autowired
	private ImageRepository imageRepo;

	private Map<TypeBook, List<Book>> sources;

	@PostConstruct
	public void init() {
		sources = new HashMap<>();
		sources.put(TypeBook.OFFICAL, repository.findBook(TypeBook.OFFICAL));
		sources.put(TypeBook.SETTING, repository.findBook(TypeBook.SETTING));
		sources.put(TypeBook.MODULE, repository.findBook(TypeBook.MODULE));
		sources.put(TypeBook.CUSTOM, repository.findBook(TypeBook.CUSTOM));
	}
	
	@GetMapping("/items/magic")
	public String getMagicItems(Model model) {
		model.addAttribute("books", sources.get(TypeBook.OFFICAL));
		model.addAttribute("settingBooks", sources.get(TypeBook.SETTING));
		model.addAttribute("moduleBooks", sources.get(TypeBook.MODULE));
		model.addAttribute("hombrewBooks", sources.get(TypeBook.CUSTOM));
		model.addAttribute("rarities", Rarity.values());
		model.addAttribute("types", MagicThingType.values());
		model.addAttribute("metaTitle", "Магические предметы (Magic items) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/items/magic");
		model.addAttribute("metaDescription", "Магические предметы и артефакты по D&D 5 редакции");
		return "items_magic";
	}
	
	@GetMapping("/items/magic/{name}")
	public String getMagicItem(Model model, @PathVariable String name, HttpServletRequest request) {
		MagicItem item = repository.findByEnglishName(name.replace("_", " "));
		if (item == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("books", sources.get(TypeBook.OFFICAL));
		model.addAttribute("settingBooks", sources.get(TypeBook.SETTING));
		model.addAttribute("moduleBooks", sources.get(TypeBook.MODULE));
		model.addAttribute("hombrewBooks", sources.get(TypeBook.CUSTOM));
		model.addAttribute("rarities", Rarity.values());
		model.addAttribute("types", MagicThingType.values());

		model.addAttribute("selectedItemMagic", new ItemMagicDto(item));
		model.addAttribute("metaTitle", item.getName() + " | Магические предметы D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/items/magic/" + name);
		model.addAttribute("metaDescription", String.format("%s (%s) - %s %s", item.getName(), item.getEnglishName(), item.getRarity().getCyrilicName(), item.getType().getCyrilicName()));
		Collection<String> images = imageRepo.findAllByTypeAndRefId(ImageType.MAGIC_ITEM, item.getId());
		if (!images.isEmpty()) {
			model.addAttribute("metaImage", images.iterator().next());
		}
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