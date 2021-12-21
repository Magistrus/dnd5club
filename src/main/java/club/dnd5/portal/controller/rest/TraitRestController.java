package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
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

import club.dnd5.portal.dto.trait.TraitDto;
import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.SkillType;
import club.dnd5.portal.model.trait.Trait;
import club.dnd5.portal.repository.datatable.TraitDatatableRepository;

@RestController
public class TraitRestController {
	@Autowired
	private TraitDatatableRepository repo;

	@GetMapping("/data/traits")
	public DataTablesOutput<TraitDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<Trait> specification = null;
		List<AbilityType> filterAbylities = Arrays.stream(input.getColumns().get(2).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).map(AbilityType::valueOf).collect(Collectors.toList());
		if (!filterAbylities.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<AbilityType, Trait> join = root.join("abilities", JoinType.LEFT);
				query.distinct(true);
				return cb.and(join.in(filterAbylities));
			});
		}
		List<SkillType> filterSkills = Arrays.stream(input.getColumns().get(3).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).map(SkillType::valueOf).collect(Collectors.toList()); 
		if (!filterSkills.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<SkillType, Trait> join = root.join("skills", JoinType.LEFT);
				query.distinct(true);
				return cb.and(join.in(filterSkills));
			});
		}
		Set<String> requirements = Arrays.stream(input.getColumns().get(4).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).collect(Collectors.toSet()); 
		if (!requirements.isEmpty()) {
			specification = addSpecification(specification,
					(root, query, cb) -> root.get("requirement").in(requirements));
		}
		return repo.findAll(input, specification, specification, TraitDto::new);
	}

	@PostMapping("/traits/")
	public TraitDto getTrait(Integer id) {
		return new TraitDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}