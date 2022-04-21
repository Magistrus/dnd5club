package club.dnd5.portal.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.classes.ClassApiDto;
import club.dnd5.portal.repository.classes.ClassRepository;

@RestController
public class ClassesApiController {
	@Autowired
	private ClassRepository classRepo;
	
	@GetMapping("/v1/api/classes")
	public List<ClassApiDto> getClasses() {
		return classRepo.findAll().stream().map(ClassApiDto::new).collect(Collectors.toList());
	}
}