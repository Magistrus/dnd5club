package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

import club.dnd5.portal.dto.bestiary.CreatureDto;
import club.dnd5.portal.model.CreatureSize;
import club.dnd5.portal.model.CreatureType;
import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.repository.datatable.BestiaryDatatableRepository;

@RestController
public class BestiaryRestController {
	@Autowired
	private BestiaryDatatableRepository repo;

	@GetMapping("/data/bestiary")
	public DataTablesOutput<CreatureDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		input.parseSearchPanesFromQueryParams(queryParameters, Arrays.asList("cr", "type", "size"));

		Specification<Creature> specification = null;
		Set<String> filterCr = input.getSearchPanes().getOrDefault("cr", Collections.emptySet()).stream()
				.map(cr -> parseCr(cr)).collect(Collectors.toSet());
		if (!filterCr.isEmpty()) {
			specification = addSpecification(specification,
					(root, query, cb) -> root.get("challengeRating").in(filterCr));
		}
		List<CreatureType> filterTypes = input.getSearchPanes().getOrDefault("type", Collections.emptySet())
				.stream()
				.map(CreatureType::valueOf)
				.collect(Collectors.toList());
		if (!filterTypes.isEmpty()) {
			specification = addSpecification(specification,  (root, query, cb) -> root.get("type").in(filterTypes));
		}
		List<CreatureSize> filterSizes = input.getSearchPanes().getOrDefault("size", Collections.emptySet())
				.stream()
				.map(CreatureSize::valueOf)
				.collect(Collectors.toList());
		if (!filterSizes.isEmpty()) {
			specification = addSpecification(specification,  (root, query, cb) -> root.get("size").in(filterSizes));
		}
		input.getSearchPanes().clear();
		DataTablesOutput<CreatureDto> output = repo.findAll(input, null, specification,
				creature -> new CreatureDto(creature));

		Map<String, List<Item>> options = new HashMap<>();
		options.put("cr", getCRs());
		options.put("type", CreatureType.getFilterTypes().stream().map(t -> new Item(t.getCyrilicName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		options.put("size", Arrays.stream(CreatureSize.values()).map(t -> new Item(t.getCyrilicName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		if (output.getSearchPanes() != null) {
			output.getSearchPanes().setOptions(options);
		}
		return output;
	}

	@PostMapping("/bestiary")
	public CreatureDto getSpell(Integer id) {
		return new CreatureDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}

	private String parseCr(String cr) {
		switch (cr) {
		case "0.5":
			return "1/2";
		case "0.25":
			return "1/4";
		case "0.125":
			return "1/8";
		default:
			return cr;
		}
	}

	private List<Item> getCRs() {
		List<Item> items = new ArrayList<>();
		items.add(new Item("  1/8","0.125", 0, 0));
		items.add(new Item("  1/4","0.25", 0, 0));
		items.add(new Item("  1/2","0.5", 0, 0));
		for (int i = 1; i <= 30; i++) {
			items.add(new Item(String.format("%2d", i), String.valueOf(i), 0, 0));
		}
		return items;
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}