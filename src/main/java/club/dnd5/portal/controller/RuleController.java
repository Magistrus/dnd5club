package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.RuleDto;
import club.dnd5.portal.model.rule.Rule;
import club.dnd5.portal.repository.datatable.RuleDatatableRepository;


@Controller
public class RuleController {
	@Autowired
	private RuleDatatableRepository repository;

	@GetMapping("/rules")
	public String getRules(Model model) {
		model.addAttribute("categories", repository.findAllCategories());
		model.addAttribute("metaTitle", "Правила и термины [Rules] D&D 5e");
		return "rules";
	}
	
	@GetMapping("/rules/{name}")
	public String getRule(Model model, @PathVariable String name) {
		Rule rule = repository.findByEnglishName(name.replace('_', ' '));
		model.addAttribute("categories", repository.findAllCategories());
		model.addAttribute("selectedRule", new RuleDto(rule));
		return "rules";
	}
	
	@GetMapping("/rules/fragment/{id}")
	public String getMagicRuleFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("rule", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/rule :: view";
	}
}