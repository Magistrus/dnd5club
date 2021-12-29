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

import club.dnd5.portal.dto.item.TreasureDto;
import club.dnd5.portal.model.items.Treasure;
import club.dnd5.portal.model.items.TreasureType;
import club.dnd5.portal.repository.datatable.TreasureDatatableRepository;

@RestController
public class TreasureRestController {
	@Autowired
	private TreasureDatatableRepository repo;

	@GetMapping("/data/treasures")
	public DataTablesOutput<TreasureDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {

		Specification<Treasure> specification = null;
		List<TreasureType> filterTypes = Arrays.stream(input.getColumns().get(2).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).map(TreasureType::valueOf).collect(Collectors.toList());
		if (!filterTypes.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("type").in(filterTypes));
		}
		return repo.findAll(input, specification, specification, TreasureDto::new);
	}

	@PostMapping("/treasures")
	public TreasureDto getSpell(Integer id) {
		return new TreasureDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}