package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.classes.OptionDto;
import club.dnd5.portal.model.classes.Option;
import club.dnd5.portal.repository.datatable.OptionDatatableRepository;

@Controller
public class OptionController {
	private static final String[] prerequsitlevels = { "Нет", " 5", " 6", " 7", " 9", "11", "12", "15", "17", "18" };

	@Autowired
	private OptionDatatableRepository repository;

	@GetMapping("/options")
	public String getOptions(Model model) {
		model.addAttribute("categories", Option.OptionType.values());
		model.addAttribute("prerequsites", repository.finAlldPrerequisite());
		model.addAttribute("levels", prerequsitlevels);
		return "options";
	}
	
	@GetMapping("/options/{name}")
	public String getOption(Model model, @PathVariable String name) {
		model.addAttribute("categories", Option.OptionType.values());
		model.addAttribute("prerequsites", repository.finAlldPrerequisite());
		model.addAttribute("levels", prerequsitlevels);
		model.addAttribute("selectedOption", new OptionDto(repository.findByEnglishName(name.replace("_", " "))));
		return "options";
	}
	
	@GetMapping("/options/fragment/{id}")
	public String getOptionFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("option", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/option :: view";
	}
}