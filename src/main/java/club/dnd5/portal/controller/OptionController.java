package club.dnd5.portal.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.naming.directory.InvalidAttributesException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.classes.OptionDto;
import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.classes.Option;
import club.dnd5.portal.model.classes.Option.OptionType;
import club.dnd5.portal.repository.classes.ClassRepository;
import club.dnd5.portal.repository.datatable.OptionDatatableRepository;

@Controller
public class OptionController {
	private static final String[] prerequsitlevels = { "Нет", " 5", " 6", " 7", " 9", "11", "12", "15", "17", "18" };

	@Autowired
	private OptionDatatableRepository repository;
	
	@Autowired
	private ClassRepository classRepository;
	
	private Map<String, String> classIcons = new HashMap<>();
	
	@PostConstruct
	public void init() {
		for(HeroClass heroClass : classRepository.findAll()) {
			classIcons.put(heroClass.getEnglishName(), heroClass.getIcon());
		}
	}
	
	@GetMapping("/options")
	public String getOptions(Model model) {
		model.addAttribute("categories", Option.OptionType.values());
		model.addAttribute("prerequsites", repository.finAlldPrerequisite());
		model.addAttribute("levels", prerequsitlevels);
		model.addAttribute("metaTitle", "Особенности классов (Options) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/options");
		model.addAttribute("metaDescription", "Список особенности классов и подкласов по D&D 5 редакции");
		return "options";
	}
	
	@GetMapping("/options/{name}")
	public String getOption(Model model, @PathVariable String name, HttpServletRequest request) {
		Option option = repository.findByEnglishName(name.replace("_", " "));
		if (option == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("categories", Option.OptionType.values());
		model.addAttribute("prerequsites", repository.finAlldPrerequisite());
		model.addAttribute("levels", prerequsitlevels);

		model.addAttribute("selectedOption", new OptionDto(option));
		model.addAttribute("metaTitle", String.format("%s (%s)", option.getName(), option.getEnglishName()) + " | Особенности классов D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/options/" + name);
		model.addAttribute("metaDescription", 
				String.format("Описание особенности %s - %s",
						option.getOptionTypes().stream().map(OptionType::getDisplayName).collect(Collectors.joining()), 
						option.getName()));
		return "options";
	}
	
	@GetMapping("/options/fragment/{id}")
	public String getOptionFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("option", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		model.addAttribute("classIcons", classIcons);
		return "fragments/option :: view";
	}
}