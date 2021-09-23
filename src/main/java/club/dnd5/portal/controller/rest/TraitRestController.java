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

import club.dnd5.portal.dto.trait.TraitDto;
import club.dnd5.portal.repository.datatable.TraitDatatableRepository;

@RestController
public class TraitRestController {
	@Autowired
	private TraitDatatableRepository repo;

	@GetMapping("/data/traits")
	public DataTablesOutput<TraitDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		return repo.findAll(input, TraitDto::new);
	}
	
	@PostMapping("/traits/") 
	public TraitDto getTrait(Integer id) {
		return new TraitDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
}