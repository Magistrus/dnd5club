package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
import org.springframework.data.jpa.datatables.mapping.SearchPanes.Item;
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
		input.parseSearchPanesFromQueryParams(queryParameters,
				Arrays.asList("abilities", "skills", "requirement", "book"));

		if (input.getSearchPanes() != null) {
			List<AbilityType> filterAbylities = input.getSearchPanes().getOrDefault("abilities", Collections.emptySet())
					.stream().map(AbilityType::valueOf).collect(Collectors.toList());
			if (!filterAbylities.isEmpty()) {
				specification = addSpecification(specification, (root, query, cb) -> {
					Join<AbilityType, Trait> abilityType = root.join("abilities", JoinType.LEFT);
					query.distinct(true);
					return cb.and(abilityType.in(filterAbylities));
				});
			}
			List<SkillType> filterSkills = input.getSearchPanes().getOrDefault("skills", Collections.emptySet())
					.stream().map(SkillType::valueOf).collect(Collectors.toList());
			if (!filterSkills.isEmpty()) {
				specification = addSpecification(specification, (root, query, cb) -> {
					Join<AbilityType, Trait> abilityType = root.join("skills", JoinType.LEFT);
					query.distinct(true);
					return cb.and(abilityType.in(filterSkills));
				});
			}
			Set<String> requirements = input.getSearchPanes().getOrDefault("requirement", Collections.emptySet());
			if (!requirements.isEmpty()) {
				specification = addSpecification(specification,
						(root, query, cb) -> root.get("requirement").in(requirements));
			}
		}
		input.getSearchPanes().remove("abilities");
		input.getSearchPanes().remove("skills");
		DataTablesOutput<TraitDto> output = repo.findAll(input, specification, specification, TraitDto::new);
		Map<String, List<Item>> options = output.getSearchPanes() == null ? new HashMap<>()
				: output.getSearchPanes().getOptions();
		options.put("abilities", Arrays.stream(AbilityType.values()).map(t -> new Item(t.getCyrilicName(), t.name(), 0, 0))
				.collect(Collectors.toList()));

		options.put("skills", Arrays.stream(SkillType.values()).map(t -> new Item(t.getCyrilicName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		if (output.getSearchPanes() != null) {
			output.getSearchPanes().setOptions(options);
		}
		return output;
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