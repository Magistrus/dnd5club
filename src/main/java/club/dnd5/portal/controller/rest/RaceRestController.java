package club.dnd5.portal.controller.rest;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.RaceDto;
import club.dnd5.portal.model.races.Race;
import club.dnd5.portal.repository.datatable.RaceDataRepository;

@RestController
public class RaceRestController {
	@Autowired
	private RaceDataRepository repo;
	 
	@GetMapping("/data/races")
	public DataTablesOutput<RaceDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters){
		Specification<Race> specification = null;
		specification = addSpecification(specification, (root, query, cb) -> cb.isNull(root.get("parent")));
		return repo.findAll(input, specification, specification, i -> new RaceDto(i));
	}

	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}