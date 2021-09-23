package club.dnd5.portal.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.repository.datatable.OptionDatatableRepository;

@Controller
public class OptionController {
	@Autowired
	private OptionDatatableRepository repository;

	@GetMapping("/options")
	public String getOptions() {
		return "options";
	}
	@GetMapping("/options/{name}")
	public String getOption(Model model, @PathVariable String name) {
		model.addAttribute("selectedOption", "name");
		return "options";
	}
	@GetMapping("/options/fragment/{id}")
	public String getOptionFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("option", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		return "fragments/option :: view";
	}
}