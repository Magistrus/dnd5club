package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
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

import club.dnd5.portal.dto.item.ItemDto;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.items.Equipment;
import club.dnd5.portal.model.items.EquipmentType;
import club.dnd5.portal.repository.datatable.ItemDatatableRepository;

@RestController
public class TreasureRestController {
	@Autowired
	private ItemDatatableRepository repo;

	@GetMapping("/data/items")
	public DataTablesOutput<ItemDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		input.parseSearchPanesFromQueryParams(queryParameters, Arrays.asList("type"));
		Specification<Equipment> specification = null;
		List<EquipmentType> filterTypes = input.getSearchPanes().getOrDefault("type", Collections.emptySet()).stream()
				.map(EquipmentType::valueOf).collect(Collectors.toList());
		if (!filterTypes.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<DamageType, Equipment> types = root.join("types", JoinType.LEFT);
				query.distinct(true);
				return types.in(filterTypes);
			});
		}
		input.getSearchPanes().clear();
		DataTablesOutput<ItemDto> output =  repo.findAll(input, specification, specification, ItemDto::new);
		
		Map<String, List<Item>> options = new HashMap<>();
		options.put("type", Arrays.stream(EquipmentType.values()).map(t -> new Item(t.getCyrilicName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		if (output.getSearchPanes() != null) {
			output.getSearchPanes().setOptions(options);
		}
		return output;
	}

	@PostMapping("/items")
	public ItemDto getSpell(Integer id) {
		return new ItemDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}