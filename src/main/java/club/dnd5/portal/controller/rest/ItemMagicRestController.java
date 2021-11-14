package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
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
		input.parseSearchPanesFromQueryParams(queryParameters, Arrays.asList("rarity", "type", "attunement", "book"));

		List<Rarity> filterRarities = input.getSearchPanes().getOrDefault("rarity", Collections.emptySet())
			.stream()
			.map(Rarity::valueOf)
			.collect(Collectors.toList());
		if (!filterRarities.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("rarity").in(filterRarities));
		}
		List<MagicThingType> filterTypes = input.getSearchPanes().getOrDefault("type", Collections.emptySet())
				.stream()
				.map(MagicThingType::valueOf)
				.collect(Collectors.toList());
		if (!filterTypes.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("type").in(filterTypes));
		}
		Set<String> customizatios = input.getSearchPanes().getOrDefault("attunement", Collections.emptySet());
		if (customizatios.contains("true")) {
			specification = addSpecification(specification, (root, query, cb) -> cb.and(cb.equal(root.get("customization"), 1)));
		} 
		if (customizatios.contains("false")) {
			specification = addSpecification(specification, (root, query, cb) -> cb.and(cb.equal(root.get("customization"), 0)));
		}
		Set<String> filterBooks = input.getSearchPanes().getOrDefault("book", Collections.emptySet());
		if (!filterBooks.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("book").get("source").in(filterBooks));
		}
		input.getSearchPanes().clear();
		DataTablesOutput<ItemMagicDto> output = repo.findAll(input, specification, specification, ItemMagicDto::new);
		
		Map<String, List<Item>> options = new HashMap<>();
		options.put("rarity", Arrays.stream(Rarity.values()).map(t -> new Item(t.getCyrilicName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		options.put("type", Arrays.stream(MagicThingType.values()).map(t -> new Item(t.getCyrilicName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		options.put("attunement", Arrays.asList(new Item("Требуется", "true", 0,0), new Item("Нет", "false", 0,0)));

		if (output.getSearchPanes() != null) {
			output.getSearchPanes().setOptions(options);
		}
		return output;
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