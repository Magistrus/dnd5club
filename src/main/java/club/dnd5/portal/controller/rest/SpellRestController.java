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
import org.springframework.data.jpa.datatables.mapping.SearchPanes.Item;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.spell.SpellDto;
import club.dnd5.portal.dto.spell.SpellTipDto;
import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.splells.MagicSchool;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;

@RestController
public class SpellRestController {
	private static final String[][] classesMap = { { "1", "Бард" }, { "2", "Волшебник" }, { "3", "Друид" },
			{ "4", "Жрец" }, { "5", "Колдун" }, { "6", "Паладин" }, { "7", "Следопыт" }, { "8", "Чародей" },
			{ "14", "Изобретатель" } };

	@Autowired
	private SpellDatatableRepository repo;

	@GetMapping("/data/spells")
	public DataTablesOutput<SpellDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		input.parseSearchPanesFromQueryParams(queryParameters,
				Arrays.asList("level", "school", "classes", "concentration", "ritual"));
		List<MagicSchool> filterSchool = input.getSearchPanes().getOrDefault("school", Collections.emptySet()).stream()
				.map(MagicSchool::valueOf).collect(Collectors.toList());
		Specification<Spell> specification = null;
		if (!filterSchool.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("school").in(filterSchool));
		}
		List<Integer> filterClasses = input.getSearchPanes().getOrDefault("classes", Collections.emptySet()).stream()
				.map(Integer::valueOf).collect(Collectors.toList());
		if (!filterClasses.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<HeroClass, Spell> hero = root.join("heroClass", JoinType.LEFT);
				query.distinct(true);
				return cb.and(hero.get("id").in(filterClasses));
			});
		}
		Set<String> concentrations = input.getSearchPanes().getOrDefault("concentration", Collections.emptySet());
		if (concentrations.contains("true")) {
			specification = addSpecification(specification,
					(root, query, cb) -> cb.equal(root.get("concentration"), true));
		}
		if (concentrations.contains("false")) {
			specification = addSpecification(specification,
					(root, query, cb) -> cb.equal(root.get("concentration"), false));
		}
		Set<String> rituals = input.getSearchPanes().getOrDefault("ritual", Collections.emptySet());
		if (rituals.contains("true") || rituals.contains("false")) {
			specification = addSpecification(specification, (root, query, cb) -> cb.equal(root.get("ritual"), rituals.contains("true")));
		}
		input.getSearchPanes().remove("school");
		input.getSearchPanes().remove("classes");
		input.getSearchPanes().remove("concentration");
		input.getSearchPanes().remove("ritual");
		DataTablesOutput<SpellDto> output = repo.findAll(input, specification, specification, SpellDto::new);
		Map<String, List<Item>> options = output.getSearchPanes().getOptions();
		options.put("school", Arrays.stream(MagicSchool.values()).map(t -> new Item(t.getName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		options.put("classes",
				Arrays.stream(classesMap).map(t -> new Item(t[1], t[0], 0, 0)).collect(Collectors.toList()));
		options.put("ritual", Arrays.asList(new Item("Да", "true", 0,0), new Item("Нет", "false", 0,0)));
		options.put("concentration", Arrays.asList(new Item("Есть", "true", 0,0), new Item("Нет", "false", 0,0)));
		output.getSearchPanes().setOptions(options);
		return output;
	}

	@PostMapping("/spells")
	public SpellTipDto getSpell(Integer id) {
		return new SpellTipDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}