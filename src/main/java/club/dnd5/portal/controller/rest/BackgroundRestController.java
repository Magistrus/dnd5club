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

import club.dnd5.portal.dto.background.BackgroundDto;
import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.SkillType;
import club.dnd5.portal.model.background.Background;
import club.dnd5.portal.model.classes.Option.OptionType;
import club.dnd5.portal.repository.datatable.BackgroundDatatableRepository;

@RestController
public class BackgroundRestController {
	@Autowired
	private BackgroundDatatableRepository repo;

	@GetMapping("/data/backgrounds")
	public DataTablesOutput<BackgroundDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<Background> specification = null;
		input.parseSearchPanesFromQueryParams(queryParameters, Arrays.asList("skills"));

		List<SkillType> filterSkills = input.getSearchPanes().getOrDefault("skills", Collections.emptySet())
			.stream()
			.map(SkillType::valueOf)
			.collect(Collectors.toList());
		if (!filterSkills.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<AbilityType, Background> abilityType = root.join("skills", JoinType.LEFT);
				return cb.and(abilityType.in(filterSkills));
			});
		}
		input.getSearchPanes().remove("skills");
		DataTablesOutput<BackgroundDto> output = repo.findAll(input, specification, specification,  BackgroundDto::new);
		Map<String, List<Item>> options = output.getSearchPanes() == null ? new HashMap<>()
				: output.getSearchPanes().getOptions();
		options.put("skills", Arrays.stream(SkillType.values()).map(t -> new Item(t.getCyrilicName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		if (output.getSearchPanes() != null) {
			output.getSearchPanes().setOptions(options);
		}
		return output;
	}
	
	@PostMapping("/backgrounds/") 
	public BackgroundDto getSpell(Integer id) {
		return new BackgroundDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
	
	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}