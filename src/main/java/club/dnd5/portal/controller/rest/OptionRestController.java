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

import club.dnd5.portal.dto.classes.OptionDto;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.classes.Option;
import club.dnd5.portal.model.classes.Option.OptionType;
import club.dnd5.portal.repository.datatable.OptionDatatableRepository;

@RestController
public class OptionRestController {
	@Autowired
	private OptionDatatableRepository repo;

	@GetMapping("/data/options")
	public DataTablesOutput<OptionDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<Option> specification = null;

		List<OptionType> optionTypes = Arrays.stream(input.getColumns().get(2).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).map(OptionType::valueOf).collect(Collectors.toList()); 
		String optionType = queryParameters.get("optionType");
		if (optionType != null) {
			optionTypes.add(OptionType.valueOf(optionType));
		}
		if (!optionTypes.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<OptionType, Option> join = root.join("optionTypes", JoinType.LEFT);
				query.distinct(true);
				return join.in(optionTypes);
			});
		}
		List<String> prerequisites  = Arrays.stream(input.getColumns().get(3).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).collect(Collectors.toList()); 
		if (!prerequisites.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("prerequisite").in(prerequisites));
		}
		if (input.getColumns().get(4).getSearch().getValue().contains("Нет")){
			specification = addSpecification(specification, (root, query, cb) -> cb.isNull(root.get("level")));
			input.getColumns().get(4).getSearch().setValue(input.getColumns().get(4).getSearch().getValue().replace("Нет", ""));
		}
		List<Integer> levels = Arrays.stream(input.getColumns().get(4).getSearch().getValue().split("\\|"))
				.map(String::trim).filter(s -> !s.isEmpty()).map(Integer::valueOf).collect(Collectors.toList()); 
		if (!levels.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("level").in(levels));
		}
		Set<String> bookSources = Arrays.stream(input.getColumns().get(5).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).collect(Collectors.toSet());
		if (!bookSources.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<Book, Object> join = root.join("book", JoinType.INNER);
				return join.get("source").in(bookSources);
			});
		}
		return repo.findAll(input, specification, specification, OptionDto::new);
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