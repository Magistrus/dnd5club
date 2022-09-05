package club.dnd5.portal.controller;

import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.ConditionDto;
import club.dnd5.portal.model.Condition;
import club.dnd5.portal.repository.ConditionRepository;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class ConditionController {
	@Autowired
	private ConditionRepository repo;

	@GetMapping("/conditions")
	public String getConditions(Model model) {
		model.addAttribute("metaTitle", "Состояния и болезни (Conditions) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/conditions");
		model.addAttribute("metaDescription", "Состояния и болезни по D&D 5 редакции");
		return "conditions";
	}

	@GetMapping("/conditions/{name}")
	public String getCondition(Model model, @PathVariable String name, HttpServletRequest request) {
		Condition condition = repo.findByEnglishName(name.replace('_', ' ')).get();
		if (condition == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("selectedCondition", new ConditionDto(condition));
		model.addAttribute("metaTitle", String.format("%s (%s) - Состояния и болезни (Conditions) D&D 5e", condition.getName(), condition.getEnglishName()));
		model.addAttribute("metaUrl", "https://dnd5.club/conditions/" + name);
		model.addAttribute("metaDescription", "Состояния и болезни по D&D 5 редакции");
		return "conditions";
	}

	@GetMapping("/conditions/fragment/{id:\\d+}")
	public String getFragmentConditionById(Model model, @PathVariable int id) {
		model.addAttribute("condition", repo.findById(id).get());
		return "fragments/condition :: view";
	}
	
	@GetMapping("/conditions/fragment/{name:[A-Za-z_]+}")
	public String getFragmentConditionByName(Model model, @PathVariable String name, HttpServletRequest request) {
		Optional<Condition> condition = repo.findByEnglishName(name.replace('_', ' '));
		if (!condition.isPresent()) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("condition", condition.get());
		return "fragments/condition :: view";
	}
}