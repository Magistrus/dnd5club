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

import club.dnd5.portal.dto.classes.OptionDto;
import club.dnd5.portal.model.classes.Option;
import club.dnd5.portal.model.classes.Option.OptionType;
import club.dnd5.portal.repository.datatable.OptionDatatableRepository;

@RestController
public class OptionRestController {
	private static final String[] prerequsitlevels = { "Нет", " 5", " 6", " 7", " 9", "11", "12", "15", "17", "18" };

	@Autowired
	private OptionDatatableRepository repo;

	@GetMapping("/data/options")
	public DataTablesOutput<OptionDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<Option> specification = null;
		input.parseSearchPanesFromQueryParams(queryParameters, Arrays.asList("type", "level", "prerequisite"));

		List<OptionType> optionTypes = input.getSearchPanes().getOrDefault("type", Collections.emptySet()).stream()
				.map(OptionType::valueOf).collect(Collectors.toList());

		if (!optionTypes.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<OptionType, Option> optionType = root.join("optionTypes", JoinType.LEFT);
				query.distinct(true);
				return optionType.in(optionTypes);
			});
		}
		List<Integer> levels = input.getSearchPanes().getOrDefault("level", Collections.emptySet()).stream()
				.map(String::trim).map(Integer::valueOf).collect(Collectors.toList());
		if (!levels.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("level").in(levels));
		}
		input.getSearchPanes().remove("type");
		input.getSearchPanes().remove("level");
		DataTablesOutput<OptionDto> output = repo.findAll(input, specification, specification, OptionDto::new);
		Map<String, List<Item>> options = output.getSearchPanes() == null ? new HashMap<>()
				: output.getSearchPanes().getOptions();

		options.put("type", Arrays.stream(OptionType.values()).map(t -> new Item(t.getName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		options.put("level",
				Arrays.stream(prerequsitlevels).map(t -> new Item(t, t, 0, 0)).collect(Collectors.toList()));
		if (output.getSearchPanes() != null) {
			output.getSearchPanes().setOptions(options);
		}
		return output;
	}

	@PostMapping("/options/")
	public OptionDto getOption(Integer id) {
		return new OptionDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}