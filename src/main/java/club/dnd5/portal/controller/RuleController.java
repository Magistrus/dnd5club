package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.RuleDto;
import club.dnd5.portal.model.rule.Rule;
import club.dnd5.portal.repository.datatable.RuleDatatableRepository;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class RuleController {
	@Autowired
	private RuleDatatableRepository repository;

	@GetMapping("/rules")
	public String getRules(Model model) {
		model.addAttribute("metaUrl", "https://dnd5.club/rules/");
		model.addAttribute("metaTitle", "Правила и термины [Rules] D&D 5e");
		model.addAttribute("metaDescription", "Правила и термины [Rules] D&D 5e");
		model.addAttribute("menuTitle", "Правила и термины");
		return "rules";
	}
	
	@GetMapping("/rules/{name}")
	public String getRule(Model model, @PathVariable String name, HttpServletRequest request) {
		Rule rule = repository.findByEnglishName(name.replace('_', ' '));
		if (rule == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("metaTitle", String.format("%s | %s | Правила и термины [Rules] D&D 5e", rule.getName(), rule.getType()));
		model.addAttribute("metaDescription", String.format("%s (%s) Правила и термины по D&D 5 редакции", rule.getName(), rule.getEnglishName()));
		model.addAttribute("metaUrl", "https://dnd5.club/rules/" + name);
		model.addAttribute("selectedRule", new RuleDto(rule));
		model.addAttribute("menuTitle", "Правила и термины");
		return "rules";
	}
	
	@GetMapping("/rules/fragment/{id}")
	public String getMagicRuleFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("rule", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/rule :: view";
	}
}