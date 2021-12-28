package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
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

import club.dnd5.portal.dto.item.ItemMagicDto;
import club.dnd5.portal.model.items.MagicItem;
import club.dnd5.portal.model.items.MagicThingType;
import club.dnd5.portal.model.items.Rarity;
import club.dnd5.portal.repository.datatable.MagicItemDatatableRepository;

@RestController
public class ItemMagicRestController {
	@Autowired
	private MagicItemDatatableRepository repo;

	@GetMapping("/data/items/magic")
	public DataTablesOutput<ItemMagicDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<MagicItem> specification = null;
		Set<Rarity> filterRarities = Arrays.stream(input.getColumns().get(0).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).map(Rarity::valueOf).collect(Collectors.toSet());  
		if (!filterRarities.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("rarity").in(filterRarities));
		}
		Set<MagicThingType> filterTypes = Arrays.stream(input.getColumns().get(3).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).map(MagicThingType::valueOf).collect(Collectors.toSet());  
		if (!filterTypes.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("type").in(filterTypes));
		}
		if (input.getColumns().get(4).getSearch().getValue().contains("1")) {
			specification = addSpecification(specification, (root, query, cb) -> cb.and(cb.equal(root.get("customization"), 1)));
		} 
		if (input.getColumns().get(4).getSearch().getValue().contains("0")) {
			specification = addSpecification(specification, (root, query, cb) -> cb.and(cb.equal(root.get("customization"), 0)));
		}
		if (input.getColumns().get(5).getSearch().getValue().contains("1")) {
			specification = addSpecification(specification, (root, query, cb) -> cb.and(cb.equal(root.get("consumed"), 1)));
		} 
		if (input.getColumns().get(5).getSearch().getValue().contains("0")) {
			specification = addSpecification(specification, (root, query, cb) -> cb.and(cb.equal(root.get("consumed"), 0)));
		}
		return repo.findAll(input, specification, specification, ItemMagicDto::new);
	}
	
	@PostMapping("/items/magic")
	public ItemMagicDto getSpell(Integer id) {
		return new ItemMagicDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}