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

import club.dnd5.portal.model.god.God;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.datatable.GodDatatableRepository;

@Controller
public class GodController {
	@Autowired
	private GodDatatableRepository repository;
	
	@Autowired
	private ImageRepository imageRepo;
	
	@GetMapping("/gods")
	public String getGods(Model model) {
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