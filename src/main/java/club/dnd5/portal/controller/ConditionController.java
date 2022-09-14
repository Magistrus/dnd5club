package club.dnd5.portal.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class ConditionController {
	@GetMapping("/conditions")
	public String getConditions(Model model) {
		return "redirect:/screens/Conditions_and_disease";
	}

	@GetMapping("/conditions/{name}")
	public String getCondition(Model model, @PathVariable String name, HttpServletRequest request) {
		return "redirect:/screens/" + name;
	}
}