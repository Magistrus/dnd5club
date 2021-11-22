package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.mapping.SearchPanes.Item;
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
		input.parseSearchPanesFromQueryParams(queryParameters, Arrays.asList("type"));
		Specification<Treasure> specification = null;
		List<TreasureType> filterTypes = input.getSearchPanes().getOrDefault("type", Collections.emptySet()).stream()
				.map(TreasureType::valueOf).collect(Collectors.toList());
		if (!filterTypes.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("type").in(filterTypes));
		}
		input.getSearchPanes().clear();
		DataTablesOutput<TreasureDto> output = repo.findAll(input, specification, specification, TreasureDto::new);

		Map<String, List<Item>> options = new HashMap<>();
		options.put("type", Arrays.stream(TreasureType.values()).map(t -> new Item(t.getName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		if (output.getSearchPanes() != null) {
			output.getSearchPanes().setOptions(options);
		}
		return output;
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