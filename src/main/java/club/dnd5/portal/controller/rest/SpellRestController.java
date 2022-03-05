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

import club.dnd5.portal.dto.spell.SpellDto;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.splells.MagicSchool;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;

@RestController
public class SpellRestController {
	@Autowired
	private SpellDatatableRepository repo;

	@GetMapping("/data/spells")
	public DataTablesOutput<SpellDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<Spell> specification = null;
		List<Integer> levels = Arrays.stream(input.getColumns().get(0).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).map(Integer::valueOf).collect(Collectors.toList());
		if (!levels.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("level").in(levels));
		}
		List<MagicSchool> filterSchool = Arrays.stream(input.getColumns().get(1).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).map(MagicSchool::valueOf).collect(Collectors.toList());
		if (!filterSchool.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("school").in(filterSchool));
		}
		List<Integer> filterClasses = Arrays.stream(input.getColumns().get(4).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).map(Integer::valueOf).collect(Collectors.toList());
		String classId = queryParameters.get("classId");
		if (classId != null) {
			filterClasses.add(Integer.valueOf(classId));
		}
		if (!filterClasses.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<HeroClass, Spell> join = root.join("heroClass", JoinType.LEFT);
				query.distinct(true);
				return cb.and(join.get("id").in(filterClasses));
			});
		}
		List<DamageType> filterDamageTypes = Arrays.stream(input.getColumns().get(5).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).map(DamageType::valueOf).collect(Collectors.toList());
		if (!filterDamageTypes.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<DamageType, Spell> join = root.join("damageType", JoinType.LEFT);
				query.distinct(true);
				return join.in(filterDamageTypes);
			});
		}
		if (input.getColumns().size() > 7) {
			List<Integer> components = Arrays.stream(input.getColumns().get(9).getSearch().getValue().split("\\|"))
					.filter(s -> !s.isEmpty()).map(Integer::valueOf).collect(Collectors.toList());
			if(!components.isEmpty()) {
				if (components.contains(1)) {
					specification = addSpecification(specification,
							(root, query, cb) -> cb.equal(root.get("verbalComponent"), true));
				}
				if (components.contains(2)) {
					specification = addSpecification(specification,
							(root, query, cb) -> cb.equal(root.get("somaticComponent"), true));
				}
				if (components.contains(3)) {
					specification = addSpecification(specification,
							(root, query, cb) -> cb.equal(root.get("materialComponent"), true));
				}
				if (components.contains(4)) {
					specification = addSpecification(specification,
							(root, query, cb) -> cb.equal(root.get("consumable"), true));
				}
			}
			Set<Integer> concentration = Arrays.stream(input.getColumns().get(8).getSearch().getValue().split("\\|"))
					.filter(s -> !s.isEmpty()).map(Integer::valueOf).collect(Collectors.toSet());
			if (components.isEmpty()) {
				if (concentration.contains(1)) {
					specification = addSpecification(specification,
							(root, query, cb) -> cb.equal(root.get("concentration"), true));
				}
				if (concentration.contains(2)) {
					specification = addSpecification(specification,
							(root, query, cb) -> cb.equal(root.get("concentration"), false));
				}
			}
			Set<Integer> rituals = Arrays.stream(input.getColumns().get(7).getSearch().getValue().split("\\|"))
					.filter(s -> !s.isEmpty()).map(Integer::valueOf).collect(Collectors.toSet());
			if (!rituals.isEmpty()) {
				if (rituals.contains(1)) {
					specification = addSpecification(specification,
							(root, query, cb) -> cb.equal(root.get("ritual"), true));
				}
				if (rituals.contains(2)) {
					specification = addSpecification(specification,
							(root, query, cb) -> cb.equal(root.get("ritual"), false));
				}
			}
			Set<String> bookSources = Arrays.stream(input.getColumns().get(10).getSearch().getValue().split("\\|"))
					.filter(s -> !s.isEmpty()).collect(Collectors.toSet());
			if (!bookSources.isEmpty()) {
				specification = addSpecification(specification, (root, query, cb) -> {
					Join<Book, Spell> join = root.join("book", JoinType.INNER);
					return join.get("source").in(bookSources);
				});
			}
		}
		return repo.findAll(input, specification, specification, SpellDto::new);
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}