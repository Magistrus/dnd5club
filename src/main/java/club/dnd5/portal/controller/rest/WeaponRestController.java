package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashSet;
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

import club.dnd5.portal.dto.item.WeaponDto;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.Dice;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.items.Weapon;
import club.dnd5.portal.model.items.WeaponProperty;
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
		Set<Integer> damages = new HashSet<>(2);
		List<Dice> filterDamageDices = Arrays.stream(input.getColumns().get(6).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.map(d -> {
					if(d.startsWith("2")) {
						damages.add(2);
						return d.replace("2", "");
					} 
					return d;
				})
				.map(Dice::valueOf)
				.collect(Collectors.toList());
		if (!filterDamageDices.isEmpty()) {
			specification = addSpecification(specification,
					(root, query, cb) -> root.get("damageDice").in(filterDamageDices));
		}
		if (damages.contains(2)) {
			specification = addSpecification(specification,
					(root, query, cb) -> root.get("numberDice").in(2));
		}
		Set<String> bookSources = Arrays.stream(input.getColumns().get(7).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).collect(Collectors.toSet());
		if (!bookSources.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<Book, Object> join = root.join("book", JoinType.INNER);
				return join.get("source").in(bookSources);
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