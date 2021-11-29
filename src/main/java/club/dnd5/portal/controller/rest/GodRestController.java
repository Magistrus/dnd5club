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

import club.dnd5.portal.dto.GodDto;
import club.dnd5.portal.model.Alignment;
import club.dnd5.portal.model.god.Domain;
import club.dnd5.portal.model.god.God;
import club.dnd5.portal.model.god.GodSex;
import club.dnd5.portal.model.god.Pantheon;
import club.dnd5.portal.model.god.Rank;
import club.dnd5.portal.repository.datatable.GodDatatableRepository;

@RestController
public class GodRestController {
	@Autowired
	private GodDatatableRepository repo;

	@GetMapping("/data/gods")
	public DataTablesOutput<GodDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<God> specification = null;
		input.parseSearchPanesFromQueryParams(queryParameters,
				Arrays.asList("alignment", "domains", "sex", "pantheon", "rank"));

		List<Alignment> filterAlignments = input.getSearchPanes().getOrDefault("alignment", Collections.emptySet())
				.stream().map(Alignment::valueOf).collect(Collectors.toList());

		if (!filterAlignments.isEmpty()) {
			specification = addSpecification(specification,
					(root, query, cb) -> root.get("aligment").in(filterAlignments));
		}
		List<Domain> filterDomains = input.getSearchPanes().getOrDefault("domains", Collections.emptySet()).stream()
				.map(Domain::valueOf).collect(Collectors.toList());
		if (!filterDomains.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<Domain, God> domain = root.join("domains", JoinType.LEFT);
				query.distinct(true);
				return domain.in(filterDomains);
			});
		}
		List<GodSex> filterSexs = input.getSearchPanes().getOrDefault("sex", Collections.emptySet()).stream()
				.map(GodSex::valueOf).collect(Collectors.toList());
		if (!filterSexs.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("sex").in(filterSexs));
		}
		Set<Integer> filterPantheons = input.getSearchPanes().getOrDefault("pantheon", Collections.emptySet()).stream()
				.map(Integer::valueOf).collect(Collectors.toSet());
		if (!filterPantheons.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<Pantheon, God> pantheon = root.join("pantheon", JoinType.LEFT);
				return cb.and(pantheon.get("id").in(filterPantheons));
			});
		}
		List<Rank> filterRanks = input.getSearchPanes().getOrDefault("rank", Collections.emptySet()).stream()
				.map(Rank::valueOf).collect(Collectors.toList());
		if (!filterRanks.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("rank").in(filterRanks));
		}
		input.getSearchPanes().clear();
		DataTablesOutput<GodDto> output = repo.findAll(input, specification, specification, i -> new GodDto(i));

		Map<String, List<Item>> options = output.getSearchPanes() == null ? new HashMap<>()
				: output.getSearchPanes().getOptions();
		options.put("alignment", Arrays.stream(Alignment.values()).map(t -> new Item(t.getCyrilicName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		options.put("domains", Arrays.stream(Domain.values()).map(t -> new Item(t.getCyrilicName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		options.put("rank", Arrays.stream(Rank.values()).map(t -> new Item(t.getName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		options.put("sex", Arrays.stream(GodSex.values()).map(t -> new Item(t.getCyrilicName(), t.name(), 0, 0))
				.collect(Collectors.toList()));
		//options.put("domains", repo.countTotalGodPantheon());
		if (output.getSearchPanes() != null) {
			output.getSearchPanes().setOptions(options);
		}
		return output;
	}

	@PostMapping("/gods/")
	public GodDto getGod(Integer id) {
		return new GodDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}