package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.util.StringUtils;

import club.dnd5.portal.dto.spell.SpellDto;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;

@Controller
public class SpellController {
	@Autowired
	private SpellDatatableRepository repository;

	@GetMapping("/spells")
	public String getSpells() {
		return "spells";
	}
	
	@GetMapping("/spells/{name}")
	public String getSpell(Model model, @PathVariable String name) {
		model.addAttribute("selectedSpell", new SpellDto(repository.findByEnglishName(name.replace("_", " "))));
		return "spells";
	}
	
	@GetMapping("/spells/fragment/{id}")
	public String getSpellFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("spell", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/spell :: view";
	}
}