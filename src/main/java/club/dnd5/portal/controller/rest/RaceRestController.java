package club.dnd5.portal.controller.rest;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.RaceDto;
import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.races.Race;
import club.dnd5.portal.repository.datatable.RaceDatatableRepository;

@RestController
public class RaceRestController {
	@Autowired
	private RaceDatatableRepository repo;
	 
	@GetMapping("/data/races")
	public DataTablesOutput<RaceDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters){
		Specification<Race> specification = null;
		List<AbilityType> abilities = Arrays.stream(input.getColumns().get(2).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.map(AbilityType::valueOf)
				.collect(Collectors.toList());
		if (!abilities.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<AbilityType, Race> join = root.join("bonuses", JoinType.LEFT);
				query.distinct(true);
				return cb.and(join.get("ability").in(abilities));
			});
		}
		if (input.getSearch().getValue().isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> cb.isNull(root.get("parent")));
		}
		Set<String> bookSources = Arrays.stream(input.getColumns().get(3).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).collect(Collectors.toSet());
		if (!bookSources.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<Book, Race> join = root.join("book", JoinType.INNER);
				return join.get("source").in(bookSources);
			});
		}
		return repo.findAll(input, specification, specification, i -> new RaceDto(i));
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}