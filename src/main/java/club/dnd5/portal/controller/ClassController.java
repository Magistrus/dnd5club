package club.dnd5.portal.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import club.dnd5.portal.dto.classes.ClassFetureDto;
import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.repository.classes.ClassRepository;

@Controller
public class ClassController {
	@Autowired
	private ClassRepository classRepository;

	@GetMapping("/classes")
	public String getClasses(Model model) {
		model.addAttribute("classes", classRepository.findAll());
		return "classes";
	}
	
	@GetMapping("/classes/fragment/{englishName}")
	public String getFragmentClasses(Model model, Device device, @PathVariable String englishName) {
		model.addAttribute("device", device);
		HeroClass heroClass = classRepository.findByEnglishName(englishName.replace("_", " "));
		List<ClassFetureDto> features = new ArrayList<>();
		heroClass.getTraits().stream()
			.filter(f -> !f.isArchitype())
			.map(f -> new ClassFetureDto(f, heroClass.getName()))
			.forEach(f -> features.add(f));
		Map<Integer, Set<ClassFetureDto>> archetypeTraits = heroClass.getArchetypes()
				.stream().flatMap(a -> a.getFeats().stream())
				.collect(
						Collectors.groupingBy(
								f -> f.getArchetype().getId(),
								Collectors.mapping(f -> new ClassFetureDto(
										f, f.getArchetype().getGenitiveName()), 
										Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ClassFetureDto::getLevel).thenComparing(ClassFetureDto::getName))))
								)
				);
		Collections.sort(features, Comparator.comparing(ClassFetureDto::getLevel));
		model.addAttribute("features", features);
		model.addAttribute("heroClass", heroClass);
		model.addAttribute("archetypeTraits", archetypeTraits);
		model.addAttribute("order", "[[ 1, 'asc' ]]");
		return "classView :: heroClass";
	}

	@GetMapping("/classes/{englishName}/architype/name")
	@ResponseBody
	public String getArchitypeName(@PathVariable String englishName) {
		HeroClass heroClass = classRepository.findByEnglishName(englishName.replace("_", " "));
		return heroClass.getArchetypeName(); 
	}
	@GetMapping("/classes/{englishName}/architypes/list")
	public String getArchitypeList(Model model, Device device, @PathVariable String englishName) {
		model.addAttribute("device", device);
		HeroClass heroClass = classRepository.findByEnglishName(englishName.replace("_", " "));
		model.addAttribute("archetypeName", heroClass.getArchetypeName());
		model.addAttribute("archetypes", heroClass.getArchetypes());
		return "archytepes :: title_sub_menu"; 
	}
}