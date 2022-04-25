package club.dnd5.portal.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.classes.ClassApiDto;
import club.dnd5.portal.repository.classes.ClassRepository;

@RestController
public class ClassesApiController {
	@Autowired
	private ClassRepository classRepo;
	
	@GetMapping(value = "/api/v1/classes", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ClassApiDto> getClasses() {
		return classRepo.findAll().stream().map(ClassApiDto::new).collect(Collectors.toList());
	}
}