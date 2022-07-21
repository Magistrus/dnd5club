package club.dnd5.portal.controller;

import java.util.Collections;

import javax.naming.directory.InvalidAttributesException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.classes.ArchetypeSpellRepository;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;

@Controller
public class SpellController {
	@Autowired
	private SpellDatatableRepository repository;
	@Autowired
	private ArchetypeSpellRepository archetypeSpellRepository;

	
	@GetMapping("/spells")
	public String getSpells(Model model) {
		model.addAttribute("metaTitle", "Заклинания (Spells) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/spells");
		model.addAttribute("metaDescription", "Заклинания по D&D 5 редакции");
		model.addAttribute("menuTitle", "Заклинания");
		return "spells";
	}
	
	@GetMapping("/spells/{name}")
	public String getSpell(Model model, @PathVariable String name, HttpServletRequest request) {
		Spell spell = repository.findByEnglishName(name.replace("_", " "));
		if (spell == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("metaTitle", String.format("%s (%s)", spell.getName(), spell.getEnglishName()) + " | Заклинания D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/spells/" + name);
		model.addAttribute("metaDescription", String.format("%s %s, %s", (spell.getLevel() == 0 ? "Заговор" : spell.getLevel() + " уровень"), spell.getName(), spell.getSchool()));
		model.addAttribute("metaImage", String.format("https://image.dnd5.club:8089/magic/%s.png", StringUtils.capitalize(spell.getSchool().name().toLowerCase())));
		model.addAttribute("menuTitle", "Заклинания");
		return "spells";
	}
	
	@GetMapping("/spells/fragment/{id}")
	public String getSpellFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("archetypes", archetypeSpellRepository.findAllBySpell(id));
		model.addAttribute("races", Collections.emptyList());
		model.addAttribute("spell", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/spell :: view";
	}
	
	@GetMapping("/spells/id")
	public String getSpell(Model model, Integer id) throws InvalidAttributesException {
		model.addAttribute("archetypes", archetypeSpellRepository.findAllBySpell(id));
		model.addAttribute("races", Collections.emptyList());
		model.addAttribute("spell", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/spell :: view";
	}
}