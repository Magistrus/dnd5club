package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		input.parseSearchPanesFromQueryParams(queryParameters, Arrays.asList("level", "school"));
		List<MagicSchool> filterSchool = input.getSearchPanes().getOrDefault("school", Collections.emptySet()).stream()
				.map(MagicSchool::valueOf).collect(Collectors.toList());
		Specification<Spell> specification = null;
		if (!filterSchool.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("school").in(filterSchool));
		}
		input.getSearchPanes().remove("school");
		DataTablesOutput<SpellDto> output = repo.findAll(input, specification, specification, SpellDto::new);
		Map<String, List<Item>> options = output.getSearchPanes().getOptions();
		options.put("school", Arrays.stream(MagicSchool.values()).map(t -> new Item(t.getName(), t.name(), 0,0)).collect(Collectors.toList()));
		output.getSearchPanes().setOptions(options);
		return output;
	}
	
	@PostMapping("/spells")
	public SpellTipDto getSpell(Integer id) {
		return new SpellTipDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}

	private <T> Specification<T> addSpecification(Specification<T> specification,
			Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}