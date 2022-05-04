package club.dnd5.portal.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.spell.SpellApiDto;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;

@RestController
public class SpellApiController {
	@Autowired
	private SpellDatatableRepository repo;
	
	@GetMapping(value = "/api/v1/spells", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SpellApiDto> getSpells() {
		DataTablesInput input = new DataTablesInput();
		input.setLength(-1);
		return repo.findAll(input).getData().stream()
				.map(SpellApiDto::new)
				.collect(Collectors.toList());
	}
}