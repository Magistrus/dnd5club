package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.RuleDto;
import club.dnd5.portal.repository.datatable.RuleDatatableRepository;

@RestController
public class RuleRestController {
	@Autowired
	private RuleDatatableRepository repo;

	@GetMapping("/data/rules")
	public DataTablesOutput<RuleDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		input.parseSearchPanesFromQueryParams(queryParameters, Arrays.asList("type"));
		return repo.findAll(input, RuleDto::new);
	}

	@PostMapping("/rules")
	public RuleDto getSpell(Integer id) {
		return new RuleDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
}