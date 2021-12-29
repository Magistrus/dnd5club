package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.RuleDto;
import club.dnd5.portal.model.rule.Rule;
import club.dnd5.portal.repository.datatable.RuleDatatableRepository;

@RestController
public class RuleRestController {
	@Autowired
	private RuleDatatableRepository repo;

	@GetMapping("/data/rules")
	public DataTablesOutput<RuleDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<Rule> specification = null;
		List<String> categories = Arrays.stream(input.getColumns().get(2).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).collect(Collectors.toList());
		if (!categories.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("type").in(categories));
		}
		return repo.findAll(input, specification, specification, RuleDto::new);
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}

	@PostMapping("/rules")
	public RuleDto getSpell(Integer id) {
		return new RuleDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
}