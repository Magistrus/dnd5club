package club.dnd5.portal.controller.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.encounters.RandomEncounter;
import club.dnd5.portal.repository.datatable.RandomEncounterRepository;

@Controller
public class RandomEncounterController {
	public static final Random rnd = new Random();
	
	@Autowired
	private RandomEncounterRepository repo;
	
	@GetMapping("/tools/encounters")
	public String getView(Model model) {
		model.addAttribute("metaTitle", "Случайные столкновения");
		model.addAttribute("metaUrl", "https://dnd5.club/tools/encounters");
		model.addAttribute("metaDescription", "Генерация случайных столкновений");
		model.addAttribute("types", HabitatType.types());
		return "tools/random_encounters";
	}
	
	@GetMapping("/tools/encounters/table")
	public String getRandomEncounters(Model model, Integer level, HabitatType type) {
		List<RandomEncounter> encounters = repo.findAllByLevelAndType(level, type);
		model.addAttribute("encounters", encounters);
		return "tools/random_encounters :: table";
	}
	
	@GetMapping("/tools/encounters/random")
	@ResponseBody
	public String getRandomEncounter(Integer level, String type) {
		HabitatType habitatType;
		if ("RANDOM".equals(type)) {
			habitatType = new ArrayList<>(HabitatType.types()).get(rnd.nextInt(HabitatType.types().size()));
		}
		else {
			habitatType = HabitatType.valueOf(type);
		}
		int index = 1 + rnd.nextInt(100);
		RandomEncounter encounter = repo.findOne(index, level, habitatType);
		return "Окружающая среда: <strong>" +  habitatType.getName() + "</strong><br>" + encounter.getDescription();
	}
}