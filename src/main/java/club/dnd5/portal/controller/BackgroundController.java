package club.dnd5.portal.controller;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.background.BackgroundDto;
import club.dnd5.portal.model.SkillType;
import club.dnd5.portal.model.background.Background;
import club.dnd5.portal.model.background.Personalization;
import club.dnd5.portal.model.background.PersonalizationType;
import club.dnd5.portal.repository.datatable.BackgroundDatatableRepository;

@Controller
public class BackgroundController {
	@Autowired
	private BackgroundDatatableRepository repository;

	@GetMapping("/backgrounds")
	public String getBackgrounds(Model model) {
		model.addAttribute("skills", SkillType.values());
		model.addAttribute("metaTitle", "Предыстории персонажей [Backgrounds] D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/backgrounds/");
		model.addAttribute("metaDescription", "Предыстории персонажей по D&D 5 редакции");
		return "backgrounds";
	}
	
	@GetMapping("/backgrounds/{name}")
	public String getBackGround(Model model, @PathVariable String name) {
		model.addAttribute("skills", SkillType.values());
		Background background = repository.findByEnglishName(name.replace("_", " "));
		model.addAttribute("selectedBackground", new BackgroundDto(background));
		model.addAttribute("metaTitle", background.getName() + " D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/backgrounds/" + name);
		model.addAttribute("metaDescription", String.format("%s (%s) - предыстория персонажа по D&D 5 редакции", background.getName(), background.getEnglishName()));
		return "backgrounds";
	}
	
	@GetMapping("/backgrounds/fragment/{id}")
	public String getBackgroundFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		Background background = repository.findById(id).orElseThrow(InvalidAttributesException::new);
		model.addAttribute("background", background);
		Map<PersonalizationType, List<Personalization>> tables = background.getPersonalizations().stream()
				.collect(Collectors.groupingBy(Personalization::getType, () -> new EnumMap<>(PersonalizationType.class), Collectors.toList()));
		model.addAttribute("tables", tables);
		return "fragments/background :: view";
	}
}