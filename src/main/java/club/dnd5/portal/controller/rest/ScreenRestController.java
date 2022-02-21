package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.ScreenDto;
import club.dnd5.portal.model.screen.Screen;
import club.dnd5.portal.repository.datatable.ScreenDatatableRepository;

@RestController
public class ScreenRestController {
	@Autowired
	private ScreenDatatableRepository repo;

	@GetMapping("/data/screens")
	public DataTablesOutput<ScreenDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<Screen> specification = null;
		if (input.getSearch().getValue().isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> cb.isNull(root.get("parent")));
		}
		return repo.findAll(input, specification, specification, ScreenDto::new);
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}

	@PostMapping("/data/screens")
	public ScreenDto getSpell(Integer id) {
		return new ScreenDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
}