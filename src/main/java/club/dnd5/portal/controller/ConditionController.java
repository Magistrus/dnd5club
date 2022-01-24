package club.dnd5.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.model.Condition.Type;
import club.dnd5.portal.repository.ConditionRepository;

@Controller
public class ConditionController {
	@Autowired
	private ConditionRepository conditionRepository;
	
	@GetMapping("/conditions")
	public String getConditions(Model model) {
		model.addAttribute("conditions", conditionRepository.findAllByType(Type.CONDITION));
		model.addAttribute("disease", conditionRepository.findAllByType(Type.DISEASE));
		model.addAttribute("other", conditionRepository.findAllByType(Type.OTHER));
		model.addAttribute("metaTitle", "Состояния и болезни [Conditions] D&D 5e");
		return "conditions";
	}

	@GetMapping("/conditions/{name}")
	public String getCondition(Model model, @PathVariable String name) {
		model.addAttribute("conditions", conditionRepository.findAllByType(Type.CONDITION));
		model.addAttribute("disease", conditionRepository.findAllByType(Type.DISEASE));
		model.addAttribute("other", conditionRepository.findAllByType(Type.OTHER));
		model.addAttribute("selectedCondition", name);
		return "conditions";
	}

	@GetMapping("/conditions/fragment/{name}")
	public String getFragmentCondition(Model model, @PathVariable String name) {
		model.addAttribute("condition", conditionRepository.findOneByEnglishName(name.replace("_", " ")));
		return "fragments/condition :: view";
	}
}