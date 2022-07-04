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

import club.dnd5.portal.dto.GodDto;
import club.dnd5.portal.model.Alignment;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.god.Domain;
import club.dnd5.portal.model.god.God;
import club.dnd5.portal.model.god.Rank;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.datatable.GodDatatableRepository;
import club.dnd5.portal.repository.datatable.PantheonGodRepository;

@Controller
public class GodController {
	@Autowired
	private GodDatatableRepository repository;
	
	@Autowired
	private PantheonGodRepository pantheonRepo;

	@Autowired
	private ImageRepository imageRepo;
	
	private Map<TypeBook, List<Book>> sources;

	@PostConstruct
	public void init() {
		sources = new HashMap<>();
		sources.put(TypeBook.OFFICAL, repository.findBook(TypeBook.OFFICAL));
		sources.put(TypeBook.SETTING, repository.findBook(TypeBook.SETTING));
		sources.put(TypeBook.CUSTOM, repository.findBook(TypeBook.CUSTOM));
	}
	
	@GetMapping("/gods")
	public String getGods(Model model) {
		model.addAttribute("books", sources.get(TypeBook.OFFICAL));
		model.addAttribute("settingBooks", sources.get(TypeBook.SETTING));
		model.addAttribute("hombrewBooks", sources.get(TypeBook.CUSTOM));
		model.addAttribute("alignments", Alignment.getGods());
		model.addAttribute("domains", Domain.values());
		model.addAttribute("ranks", Rank.values());
		model.addAttribute("pantheons", pantheonRepo.findAll());
		model.addAttribute("metaTitle", "Боги (Gods) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/gods");
		model.addAttribute("metaDescription", "Боги, полубоги и философии D&D 5 редакции");
		model.addAttribute("menuTitle", "Боги");
		return "gods";
	}
	
	@GetMapping("/gods/{name}")
	public String getGod(Model model, @PathVariable String name, HttpServletRequest request) {
		God god = repository.findByEnglishName(name.replace("_", " "));
		if (god == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("books", sources.get(TypeBook.OFFICAL));
		model.addAttribute("settingBooks", sources.get(TypeBook.SETTING));
		model.addAttribute("moduleBooks", sources.get(TypeBook.MODULE));
		model.addAttribute("hombrewBooks", sources.get(TypeBook.CUSTOM));
		model.addAttribute("alignments", Alignment.getGods());
		model.addAttribute("domains", Domain.values());
		model.addAttribute("ranks", Rank.values());
		model.addAttribute("pantheons", pantheonRepo.findAll());

		model.addAttribute("selectedGod", new GodDto(god));
		model.addAttribute("metaTitle", god.getName() + " | Боги D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/gods/" + name);
		model.addAttribute("metaDescription", String.format("%s (%s) - %s %s, %s", god.getName(), god.getEnglishName(), god.getAligment().getCyrilicName(), god.getSex().getCyrilicName(), god.getCommitment()));
		Collection<String> images = imageRepo.findAllByTypeAndRefId(ImageType.GOD, god.getId());
		if (!images.isEmpty()) {
			model.addAttribute("metaImage", images.iterator().next());
		}
		model.addAttribute("menuTitle", "Боги");
		return "gods";
	}
	
	@GetMapping("/gods/fragment/{id}")
	public String getGodFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		God god = repository.findById(id).orElseThrow(InvalidAttributesException::new);
		model.addAttribute("god", god);
		Collection<String> images = imageRepo.findAllByTypeAndRefId(ImageType.GOD, god.getId());
		model.addAttribute("images", images);
		return "fragments/god :: view";
	}
}