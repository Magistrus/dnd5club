package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.bestiary.CreatureDto;
import club.dnd5.portal.model.CreatureSize;
import club.dnd5.portal.model.CreatureType;
import club.dnd5.portal.repository.datatable.BestiaryDatatableRepository;
import club.dnd5.portal.repository.datatable.TagBestiaryDatatableRepository;

@Controller
public class BestiaryController {
	@Autowired
	private BestiaryDatatableRepository repository;
	
	@Autowired
	private TagBestiaryDatatableRepository tagRepo;
	
	@GetMapping("/bestiary")
	public String getCreatures(Model model) {
		model.addAttribute("types", CreatureType.getFilterTypes());
		model.addAttribute("sizes", CreatureSize.getFilterSizes());
		model.addAttribute("tags", tagRepo.findAll());
		return "bestiary";
	}
	
	@GetMapping("/bestiary/{name}")
	public String getCreature(Model model, @PathVariable String name) {
		model.addAttribute("selectedCreature", new CreatureDto(repository.findByEnglishName(name.replace("_", " "))));
		model.addAttribute("types", CreatureType.getFilterTypes());
		model.addAttribute("sizes", CreatureSize.getFilterSizes());
		model.addAttribute("tags", tagRepo.findAll());
		return "bestiary";
	}
	
	@GetMapping("/bestiary/fragment/{id}")
	public String getCreatureFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("creature", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		model.addAttribute("firstElement", new FirstElement());
		return "fragments/creature :: view";
	}
	
	@GetMapping("/bestiary/description/{id}")
	public String getCreatureDescription(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("creature", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		model.addAttribute("firstElement", new FirstElement());
		return "fragments/creature :: description";
	}

	public static class FirstElement{
		private boolean first = true;
		public boolean isFirst() {
			boolean first = this.first;
			this.first = false;
			return first;
		}
	}
}
