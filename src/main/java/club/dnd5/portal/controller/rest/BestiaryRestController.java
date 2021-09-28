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

import club.dnd5.portal.dto.bestiary.CreatureDto;
import club.dnd5.portal.repository.datatable.BestiaryDatatableRepository;

@RestController
public class BestiaryRestController {
	@Autowired
	private BestiaryDatatableRepository repo;

	@GetMapping("/data/bestiary")
	public DataTablesOutput<CreatureDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		return repo.findAll(input, CreatureDto::new);
	}
	
	@PostMapping("/bestiary")
	public CreatureDto getSpell(Integer id) {
		return new CreatureDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
}