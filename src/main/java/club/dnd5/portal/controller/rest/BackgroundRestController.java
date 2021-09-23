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

import club.dnd5.portal.dto.background.BackgroundDto;
import club.dnd5.portal.repository.datatable.BackgroundDatatableRepository;

@RestController
public class BackgroundRestController {
	@Autowired
	private BackgroundDatatableRepository repo;

	@GetMapping("/data/backgrounds")
	public DataTablesOutput<BackgroundDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		return repo.findAll(input, BackgroundDto::new);
	}
	
	@PostMapping("/backgrounds/") 
	public BackgroundDto getSpell(Integer id) {
		return new BackgroundDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
}