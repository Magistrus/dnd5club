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

import club.dnd5.portal.dto.item.WeaponDto;
import club.dnd5.portal.repository.datatable.WeaponDatatableRepository;

@RestController
public class WeaponRestController {
	@Autowired
	private WeaponDatatableRepository repo;

	@GetMapping("/data/weapons")
	public DataTablesOutput<WeaponDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		return repo.findAll(input, WeaponDto::new);
	}
	
	@PostMapping("/weapons")
	public WeaponDto getSpell(Integer id) {
		return new WeaponDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
}