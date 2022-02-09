package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

import club.dnd5.portal.dto.background.BackgroundDto;
import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.SkillType;
import club.dnd5.portal.model.background.Background;
import club.dnd5.portal.repository.datatable.BackgroundDatatableRepository;

@RestController
public class BackgroundRestController {
	@Autowired
	private BackgroundDatatableRepository repo;

	@GetMapping("/data/backgrounds")
	public DataTablesOutput<BackgroundDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<Background> specification = null;

		List<SkillType> filterSkills = Arrays.stream(input.getColumns().get(2).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).map(SkillType::valueOf).collect(Collectors.toList()); 
		if (!filterSkills.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<AbilityType, Background> abilityType = root.join("skills", JoinType.LEFT);
				query.distinct(true);
				return cb.and(abilityType.in(filterSkills));
			});
		}
		return  repo.findAll(input, specification, specification,  BackgroundDto::new);
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