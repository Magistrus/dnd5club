package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.bestiary.CreatureDto;
import club.dnd5.portal.model.CreatureSize;
import club.dnd5.portal.model.CreatureType;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.foundary.FCreature;
import club.dnd5.portal.model.races.Race;
import club.dnd5.portal.repository.datatable.BestiaryDatatableRepository;

@RestController
public class BestiaryRestController {
	@Autowired
	private BestiaryDatatableRepository repo;
	
	@GetMapping("/data/bestiary")
	public DataTablesOutput<CreatureDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		Specification<Creature> specification = "true".equals(queryParameters.get("npc"))  ? null : (root, query, cb) -> cb.notEqual(root.get("raceId"), 102);
		Set<String> filterCr = Arrays.stream(input.getColumns().get(3).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toSet());
		if (!filterCr.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> root.get("challengeRating").in(filterCr));
		}
		List<CreatureType> filterTypes = Arrays.stream(input.getColumns().get(4).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.map(CreatureType::valueOf)
				.collect(Collectors.toList());
		if (!filterTypes.isEmpty()) {
			specification = addSpecification(specification,  (root, query, cb) -> root.get("type").in(filterTypes));
		}
		List<CreatureSize> filterSizes = Arrays.stream(input.getColumns().get(5).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.map(CreatureSize::valueOf)
				.collect(Collectors.toList());
		if (!filterSizes.isEmpty()) {
			specification = addSpecification(specification,  (root, query, cb) -> root.get("size").in(filterSizes));
		}
		List<Integer> tagIds = Arrays.stream(input.getColumns().get(6).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.map(Integer::valueOf)
				.collect(Collectors.toList());
		if (!tagIds.isEmpty()) {
			specification = addSpecification(specification,  (root, query, cb) -> {
				Join<Object, Object> join = root.join("races", JoinType.INNER);
				query.distinct(true);
				return cb.and(join.get("id").in(tagIds));
			});
		}
		List<HabitatType> habitateTypes = Arrays.stream(input.getColumns().get(7).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty())
				.map(HabitatType::valueOf)
				.collect(Collectors.toList());
		if (!habitateTypes.isEmpty()) {
			specification = addSpecification(specification,  (root, query, cb) -> {
				Join<Object, Object> join = root.join("habitates", JoinType.INNER);
				query.distinct(true);
				return join.in(habitateTypes);
			});
		}
		Set<String> bookSources = Arrays.stream(input.getColumns().get(9).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).collect(Collectors.toSet());
		if (!bookSources.isEmpty()) {
			specification = addSpecification(specification, (root, query, cb) -> {
				Join<Book, Object> join = root.join("book", JoinType.INNER);
				return join.get("source").in(bookSources);
			});
		}
		return repo.findAll(input, null, specification, creature -> new CreatureDto(creature));
	}

	@PostMapping("/bestiary")
	public CreatureDto getSpell(Integer id) {
		return new CreatureDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}

	@GetMapping("/creature/json/{id}")
	public ResponseEntity<FCreature> getCreature(HttpServletResponse response, @PathVariable Integer id){
		Creature creature = repo.findById(id).get();
		response.setContentType("application/json");
		String file = String.format("attachment; filename=\"%s.json\"", creature.getEnglishName());
		response.setHeader("Content-Disposition", file); 
		return ResponseEntity.ok(new FCreature(creature));
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}