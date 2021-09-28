package club.dnd5.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.model.races.Race;
import club.dnd5.portal.repository.classes.RaceRepository;

@Controller
public class RaceController {
	@Autowired
	private RaceRepository raceRepository;
	
	@GetMapping("/races")
	public String getRaces(Model model) {
		model.addAttribute("races", raceRepository.findAllByParent(null, Sort.by("name")));
		return "races";
	}
	
	@GetMapping("/races/{name}")
	public String getRace(Model model, @PathVariable String name) {
		model.addAttribute("races", raceRepository.findAllByParent(null, Sort.by("name")));
		model.addAttribute("selectedRace", name);
		return "races";
	}
	
	@GetMapping("/races/{name}/{subrace}")
	public String getSubraces(Model model, @PathVariable String name, @PathVariable String subrace) {
		model.addAttribute("races", raceRepository.findAllByParent(null, Sort.by("name")));
		model.addAttribute("selectedRace", name);
		model.addAttribute("selectedSubrace", subrace);
		return "races";
	}
	
	@GetMapping("/races/fragment/{englishName}")
	public String getFragmentRace(Model model, Device device, @PathVariable String englishName) {
		Race race = raceRepository.findByEnglishName(englishName.replace("_", " ")).orElseThrow(IllegalArgumentException::new);
		model.addAttribute("race", race);
		return "fragments/race :: view";
	}
	
	@GetMapping("/races/{raceName}/subrace/{subraceName}")
	public String getFragmentSubraces(Model model, Device device, @PathVariable String raceName, @PathVariable String subraceName) {
		Race race = raceRepository.findByEnglishName(raceName.replace("_", " ")).orElseThrow(IllegalArgumentException::new);
		model.addAttribute("race", race.getSubRaces()
				.stream()
				.filter(r-> r.getEnglishName().equalsIgnoreCase(subraceName.replace("_", " ")))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new));
		return "fragments/race :: view";
	}
	
	@GetMapping("/races/{englishName}/subraces/list")
	public String getArchitypeList(Model model, Device device, @PathVariable String englishName) {
		model.addAttribute("device", device);
		Race race = raceRepository.findByEnglishName(englishName.replace("_", " ")).orElseThrow(IllegalArgumentException::new);
		model.addAttribute("subraces", race.getSubRaces());
		return "fragments/subraces_list :: sub_menu"; 
	}
}