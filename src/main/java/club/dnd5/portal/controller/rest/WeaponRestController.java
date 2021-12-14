package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
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

import club.dnd5.portal.dto.item.WeaponDto;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.items.Weapon;
import club.dnd5.portal.repository.datatable.WeaponDatatableRepository;

@RestController
public class WeaponRestController {
	@Autowired
	private WeaponDatatableRepository repo;

	@GetMapping("/data/weapons")
	public DataTablesOutput<WeaponDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		input.parseSearchPanesFromQueryParams(queryParameters, Arrays.asList("damageType", "properties"));
		Specification<Weapon> specification = null;
		List<DamageType> filterDamageTypes = Arrays.stream(input.getColumns().get(3).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.map(DamageType::valueOf)
				.collect(Collectors.toList());
		if (!filterDamageTypes.isEmpty()) {
			specification = addSpecification(specification,
					(root, query, cb) -> root.get("damageType").in(filterDamageTypes));
		}
		input.getSearchPanes().clear();
		DataTablesOutput<WeaponDto> output = repo.findAll(input, specification, specification, WeaponDto::new);
		Map<String, List<Item>> options = output.getSearchPanes() == null ? new HashMap<>()
				: output.getSearchPanes().getOptions();
		options.put("damageType", DamageType.getWeaponDamage().stream()
				.map(t -> new Item(t.getCyrilicName(), t.name(), 0, 0)).collect(Collectors.toList()));
		if (output.getSearchPanes() != null) {
			output.getSearchPanes().setOptions(options);
		}
		return output;
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