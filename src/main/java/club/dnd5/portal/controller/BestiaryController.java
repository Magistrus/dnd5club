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

import club.dnd5.portal.dto.bestiary.CreatureDto;
import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.datatable.BestiaryDatatableRepository;

@Controller
public class BestiaryController {
	@Autowired
	private BestiaryDatatableRepository repository;
	
	@Autowired
	private ImageRepository imageRepo;

	@GetMapping("/bestiary")
	public String getCreatures(Model model) {
		model.addAttribute("metaTitle", "Бестиарий (Monster Manual) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/bestiary");
		model.addAttribute("metaDescription", "Бестиарий - существа для D&D 5 редакции");
		model.addAttribute("menuTitle", "Бестиарий");
		return "bestiary";
	}
	
	@GetMapping("/bestiary/{name}")
	public String getCreature(Model model, @PathVariable String name, HttpServletRequest request) {
		Creature beast = repository.findByEnglishName(name.replace("_", " "));
		if (beast == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		CreatureDto creature = new CreatureDto(beast);
		model.addAttribute("metaTitle", String.format("%s (%s)", creature.getName(), creature.getEnglishName()) + " | Бестиарий D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/bestiary/" + name);
		model.addAttribute("metaDescription", String.format("%s (%s) - %s %s, %s с уровнем опасности %s", beast.getName(), beast.getEnglishName(), beast.getSizeName(), beast.getType().getCyrilicName(), beast.getAligment(), beast.getChallengeRating()));
		Collection<String> images = imageRepo.findAllByTypeAndRefId(ImageType.CREATURE, creature.getId());
		if (!images.isEmpty()) {
			model.addAttribute("metaImage", images.iterator().next());
		}
		model.addAttribute("menuTitle", "Бестиарий");
		return "bestiary";
	}
	
	@GetMapping("/bestiary/fragment/{id:\\d+}")
	public String getCreatureFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		Creature creature = repository.findById(id).orElseThrow(InvalidAttributesException::new);
		model.addAttribute("creature", creature);
		Collection<String> images = imageRepo.findAllByTypeAndRefId(ImageType.CREATURE, creature.getId());
		model.addAttribute("images", images);
		return "fragments/creature :: view";
	}
	
	@GetMapping("/bestiary/description/{id:\\d+}")
	public String getCreatureDescription(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("creature", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/creature :: description";
	}
}