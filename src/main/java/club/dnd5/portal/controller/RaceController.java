package club.dnd5.portal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.model.races.Feature;
import club.dnd5.portal.model.races.Race;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.classes.RaceRepository;

@Controller
public class RaceController {
	@Autowired
	private RaceRepository raceRepository;
	@Autowired
	private ImageRepository imageRepository;
	
	@GetMapping("/races")
	public String getRaces(Model model) {
		model.addAttribute("races", raceRepository.findAllByParent(null, getRaceSort()));
		return "races";
	}
	
	@GetMapping("/races/{name}")
	public String getRace(Model model, @PathVariable String name) {
		model.addAttribute("races", raceRepository.findAllByParent(null, getRaceSort()));
		model.addAttribute("selectedRace", name);
		return "races";
	}
	
	@GetMapping("/races/{name}/{subrace}")
	public String getSubraceList(Model model, @PathVariable String name, @PathVariable String subrace) {
		model.addAttribute("races", raceRepository.findAllByParent(null, getRaceSort()));
		model.addAttribute("selectedRace", name);
		model.addAttribute("selectedSubrace", subrace);
		return "races";
	}
	
	@GetMapping("/races/fragment/{englishName}")
	public String getFragmentRace(Model model, Device device, @PathVariable String englishName) {
		Race race = raceRepository.findByEnglishName(englishName.replace("_", " ")).orElseThrow(IllegalArgumentException::new);
		List<Feature> features =  race.getFeatures().stream().filter(Feature::isFeature).collect(Collectors.toList());
		model.addAttribute("features", features);
		model.addAttribute("race", race);
		return "fragments/race :: view";
	}
	
	@GetMapping("/races/{raceName}/subrace/{subraceName}")
	public String getFragmentSubraces(Model model, Device device, @PathVariable String raceName, @PathVariable String subraceName) {
		Race race = raceRepository.findByEnglishName(raceName.replace("_", " ")).orElseThrow(IllegalArgumentException::new);
		Set<Integer> replace = race.getFeatures().stream().map(Feature::getReplaceFeatureId).filter(Objects::nonNull).collect(Collectors.toSet());
		List<Feature> features =  race.getSubRaces()
				.stream()
				.filter(r-> r.getEnglishName().equalsIgnoreCase(subraceName.replace("_", " ")))
				.flatMap(r -> Stream.concat(
						r.getParent().getFeatures().stream()
							.filter(f -> !replace.contains(f.getId())).filter(f -> f.isFeature()),
						r.getFeatures().stream().filter(f -> f.isFeature())
					)
				)
				.collect(Collectors.toList());
		model.addAttribute("features", features);
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
		model.addAttribute("images", imageRepository.findAllByTypeAndRefId(ImageType.RACE, race.getId()));
		model.addAttribute("race", race);
		model.addAttribute("subraces", race.getSubRaces());
		return "fragments/subraces_list :: sub_menu"; 
	}
	
	@GetMapping("/races/{name}/description")
	public String getRaceDescription(Model model, @PathVariable String name) {
		Race race = raceRepository.findByEnglishName(name.replace("_", " ")).orElseThrow(IllegalArgumentException::new);
		model.addAttribute("race", race);
		List<Feature> features =  race.getFeatures().stream().filter(f -> !f.isFeature()).collect(Collectors.toList());
		model.addAttribute("features", features);
		return "fragments/race_description :: view";
	}
	
	@GetMapping("/races/{className}/subrace/{subraceName}/description")
	public String getSubraceDescription(Model model, @PathVariable String className, @PathVariable String subraceName) {
		Race race = raceRepository.findByEnglishName(subraceName.replace("_", " ")).orElseThrow(IllegalArgumentException::new);
		model.addAttribute("race", race);
		Set<Integer> replace = race.getParent().getFeatures().stream().map(Feature::getReplaceFeatureId).filter(Objects::nonNull).collect(Collectors.toSet());
		List<Feature> features =   Stream.concat(
						race.getParent().getFeatures().stream()
							.filter(f -> !replace.contains(f.getId())).filter(f -> !f.isFeature()),
						race.getFeatures().stream().filter(f -> !f.isFeature())
					)
				.collect(Collectors.toList());
		model.addAttribute("features", features);
		return "fragments/race_description :: view";
	}
	
	private Sort getRaceSort() {
		List<Order> orders = new ArrayList<>(1);
		//Order order1 = new Order(Sort.Direction.DESC, "book.type");
		//orders.add(order1);
		Order order2 = new Order(Sort.Direction.ASC, "name");
		orders.add(order2);
		return Sort.by(orders);
	}
}