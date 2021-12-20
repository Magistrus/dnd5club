package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.spell.SpellDto;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.splells.MagicSchool;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;

@Controller
public class SpellController {
	private static final String[][] classesMap = { { "1", "Бард" }, { "2", "Волшебник" }, { "3", "Друид" },
			{ "4", "Жрец" }, { "5", "Колдун" }, { "6", "Паладин" }, { "7", "Следопыт" }, { "8", "Чародей" },
			{ "14", "Изобретатель" } };
	
	@Autowired
	private SpellDatatableRepository repository;

	@GetMapping("/spells")
	public String getSpells(Model model) {
		model.addAttribute("classes", classesMap);
		model.addAttribute("schools", MagicSchool.values());
		model.addAttribute("damageTypes", DamageType.getSpellDamage());
		return "spells";
	}
	
	@GetMapping("/spells/{name}")
	public String getSpell(Model model, @PathVariable String name) {
		model.addAttribute("classes", classesMap);
		model.addAttribute("schools", MagicSchool.values());
		model.addAttribute("damageTypes", DamageType.getSpellDamage());
		model.addAttribute("selectedSpell", new SpellDto(repository.findByEnglishName(name.replace("_", " "))));
		return "spells";
	}
	
	@GetMapping("/spells/fragment/{id}")
	public String getSpellFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("spell", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/spell :: view";
	}
}