package club.dnd5.portal.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.naming.directory.InvalidAttributesException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.model.classes.Option;
import club.dnd5.portal.model.classes.Option.OptionType;
import club.dnd5.portal.repository.datatable.OptionDatatableRepository;

@Controller
public class OptionController {
	@Autowired
	private OptionDatatableRepository repository;
	
	private Map<String, String> classIcons = new HashMap<>();
	
	@GetMapping("/options")
	public String getOptions(Model model) {
		model.addAttribute("metaTitle", "Особенности классов (Options) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/options");
		model.addAttribute("metaDescription", "Список особенности классов и подкласов по D&D 5 редакции");
		model.addAttribute("menuTitle", "Особенности классов");
		return "options";
	}
	
	@GetMapping("/options/{name}")
	public String getOption(Model model, @PathVariable String name, HttpServletRequest request) {
		Option option = repository.findByEnglishName(name.replace("_", " "));
		if (option == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("metaTitle", String.format("%s (%s)", option.getName(), option.getEnglishName()) + " | Особенности классов D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/options/" + name);
		model.addAttribute("metaDescription", 
				String.format("Описание особенности %s - %s",
						option.getOptionTypes().stream().map(OptionType::getDisplayName).collect(Collectors.joining()), 
						option.getName()));
		model.addAttribute("menuTitle", "Особенности классов");
		return "options";
	}
	
	@GetMapping("/options/fragment/{id}")
	public String getOptionFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("option", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		model.addAttribute("classIcons", classIcons);
		return "fragments/option :: view";
	}
}