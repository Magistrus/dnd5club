package club.dnd5.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import club.dnd5.portal.repository.classes.RaceRepository;

@Controller
public class RaceController {
	@Autowired
	private RaceRepository raceRepository;
	
	@GetMapping("/races")
	public String getRaces(Model model) {
		model.addAttribute("races", raceRepository.findAll());
		return "races";
	}
}