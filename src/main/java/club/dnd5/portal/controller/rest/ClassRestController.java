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

import club.dnd5.portal.dto.classes.ClassDto;
import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.repository.datatable.ClassDataRepository;

@RestController
public class ClassRestController {
	@Autowired
	private ClassDataRepository repo;
	 
	@GetMapping("/data/classes")
	public DataTablesOutput<ClassDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters){
		Specification<HeroClass> specification = null;
		return repo.findAll(input, specification, specification, i -> new ClassDto(i));
	}
}