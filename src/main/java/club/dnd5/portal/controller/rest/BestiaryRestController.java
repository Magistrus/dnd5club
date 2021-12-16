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
		Specification<Creature> specification = null;
		Set<String> filterCr = Arrays.stream(input.getColumns().get(3).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toSet());
		if (!filterCr.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("challengeRating").in(filterCr));
		}
		List<CreatureType> filterTypes = Arrays.stream(input.getColumns().get(4).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.map(CreatureType::valueOf)
				.collect(Collectors.toList());
		if (!filterTypes.isEmpty()) {
			specification = addSpecification(specification,  (root, query, cb) -> root.get("type").in(filterTypes));
		}
		List<CreatureSize> filterSizes = Arrays.stream(input.getColumns().get(5).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.map(CreatureSize::valueOf)
				.collect(Collectors.toList());
		if (!filterSizes.isEmpty()) {
			specification = addSpecification(specification,  (root, query, cb) -> root.get("size").in(filterSizes));
		}
		List<Integer> tagIds = Arrays.stream(input.getColumns().get(6).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.map(Integer::valueOf)
				.collect(Collectors.toList());
		if (!tagIds.isEmpty()) {
			specification = addSpecification(specification,  (root, query, cb) -> {
				Join<Object, Object> join = root.join("races", JoinType.INNER);
				query.distinct(true);
				return cb.and(join.get("id").in(tagIds));
			});
		}
		return repo.findAll(input, null, specification, creature -> new CreatureDto(creature));
	}

	@PostMapping("/bestiary")
	public CreatureDto getSpell(Integer id) {
		return new CreatureDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}