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

import club.dnd5.portal.dto.GodDto;
import club.dnd5.portal.repository.datatable.GodDatatableRepository;

@RestController
public class GodRestController {
	@Autowired
	private GodDatatableRepository repo;

	@GetMapping("/data/gods")
	public DataTablesOutput<GodDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		return repo.findAll(input, GodDto::new);
	}
	
	@PostMapping("/gods/") 
	public GodDto getGod(Integer id) {
		return new GodDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
}