package club.dnd5.portal.controller.api;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.classes.ClassApiDto;
import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.classes.ClassRepository;

@RestController
public class ClassesApiController {
	@Autowired
	private ClassRepository classRepo;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@GetMapping(value = "/api/v1/classes", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ClassApiDto> getClasses() {
		return classRepo.findAll().stream().map(ClassApiDto::new).collect(Collectors.toList());
	}
	
	@GetMapping(value = "/api/v1/classes/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ClassInfoApiDto getClassInfo(@PathVariable String englishName) {
		HeroClass heroClass = classRepo.findByEnglishName(englishName);
		Collection<String> images = imageRepository.findAllByTypeAndRefId(ImageType.CLASS, heroClass.getId());
		return new ClassInfoApiDto(heroClass, images);
	}
}