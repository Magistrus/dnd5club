package club.dnd5.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.model.Condition;
import club.dnd5.portal.repository.ConditionRepository;

@Controller
public class ConditionController {
	@Autowired
	private ConditionRepository repo;
	
	@GetMapping("/conditions")
	public String getConditions(Model model) {
		model.addAttribute("metaTitle", "Состояния и болезни (Conditions) D&D 5e");
		return "conditions";
	}

	@GetMapping("/conditions/{name}")
	public String getCondition(Model model, @PathVariable String name) {
		Condition condition = repo.findByEnglishName(name.replace('_', ' ')).get();
		model.addAttribute("selectedCondition", condition);
		return "conditions";
	}

	@GetMapping("/conditions/fragment/{id}")
	public String getFragmentCondition(Model model, @PathVariable int id) {
		model.addAttribute("condition", repo.findById(id).get());
		return "fragments/condition :: view";
	}
}