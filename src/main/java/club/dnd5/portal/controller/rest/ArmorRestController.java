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

import club.dnd5.portal.dto.item.ArmorDto;
import club.dnd5.portal.repository.datatable.ArmorDatatableRepository;

@RestController
public class ArmorRestController {
	@Autowired
	private ArmorDatatableRepository repo;

	@GetMapping("/data/armors")
	public DataTablesOutput<ArmorDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		return repo.findAll(input, ArmorDto::new);
	}
	
	@PostMapping("/armors")
	public ArmorDto getArmor(Integer id) {
		return new ArmorDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
}