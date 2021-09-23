package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.classes.OptionDto;
import club.dnd5.portal.repository.datatable.OptionDatatableRepository;

@RestController
public class OptionRestController {
	@Autowired
	private OptionDatatableRepository repo;

	@GetMapping("/data/options")
	public DataTablesOutput<OptionDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		return repo.findAll(input, OptionDto::new);
	}
	
	@PostMapping("/options/") 
	public OptionDto getSpell(Integer id) {
		return new OptionDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
}