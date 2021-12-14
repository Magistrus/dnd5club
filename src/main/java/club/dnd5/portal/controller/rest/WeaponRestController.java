package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
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

import club.dnd5.portal.dto.item.WeaponDto;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.items.Weapon;
import club.dnd5.portal.model.items.WeaponProperty;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.datatable.WeaponDatatableRepository;

@RestController
public class WeaponRestController {
	@Autowired
	private WeaponDatatableRepository repo;

	@GetMapping("/data/weapons")
	public DataTablesOutput<WeaponDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<Weapon> specification = null;
		List<DamageType> filterDamageTypes = Arrays.stream(input.getColumns().get(3).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.map(DamageType::valueOf)
				.collect(Collectors.toList());
		if (!filterDamageTypes.isEmpty()) {
			specification = addSpecification(specification,
					(root, query, cb) -> root.get("damageType").in(filterDamageTypes));
		}
		List<Integer> filterPropertyIds = Arrays.stream(input.getColumns().get(4).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.map(Integer::valueOf)
				.collect(Collectors.toList());
		if (!filterPropertyIds.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<WeaponProperty, Weapon> join = root.join("properties", JoinType.LEFT);
				query.distinct(true);
				return cb.and(join.get("id").in(filterPropertyIds));
			});
		}
		return  repo.findAll(input, specification, specification, WeaponDto::new);
	}

	@PostMapping("/weapons")
	public WeaponDto getWeapon(Integer id) {
		return new WeaponDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}