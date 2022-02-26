package club.dnd5.portal.controller.rest;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.ConditionDto;
import club.dnd5.portal.model.Condition;
import club.dnd5.portal.repository.datatable.ConditionDatatableRepository;

@RestController
public class ConditionRestController {
	@Autowired
	private ConditionDatatableRepository repo;

	@GetMapping("/data/conditions")
	public DataTablesOutput<ConditionDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<Condition> specification = null;

		return repo.findAll(input, specification, specification, ConditionDto::new);
	}
}