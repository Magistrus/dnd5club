package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.item.ItemDto;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.items.Equipment;
import club.dnd5.portal.model.items.EquipmentType;
import club.dnd5.portal.repository.datatable.ItemDatatableRepository;

@RestController
public class ItemRestController {
	@Autowired
	private ItemDatatableRepository repo;

	@GetMapping("/data/items")
	public DataTablesOutput<ItemDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<Equipment> specification = null;
		List<EquipmentType> filterTypes = Arrays.stream(input.getColumns().get(2).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.map(EquipmentType::valueOf).collect(Collectors.toList());
		if (!filterTypes.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<DamageType, Equipment> types = root.join("types", JoinType.LEFT);
				query.distinct(true);
				return types.in(filterTypes);
			});
		}
		Set<String> bookSources = Arrays.stream(input.getColumns().get(3).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).collect(Collectors.toSet());
		if (!bookSources.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<Book, Object> join = root.join("book", JoinType.INNER);
				return join.get("source").in(bookSources);
			});
		}
		return repo.findAll(input, specification, specification, ItemDto::new);
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