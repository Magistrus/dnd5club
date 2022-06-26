package club.dnd5.portal.controller.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.FilterApi;
import club.dnd5.portal.dto.api.FilterValueApi;
import club.dnd5.portal.dto.api.classes.ClassApi;
import club.dnd5.portal.dto.api.classes.ClassInfoApiDto;
import club.dnd5.portal.dto.api.classes.ClassRequestApi;
import club.dnd5.portal.model.Dice;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.classes.archetype.Archetype;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.classes.ArchetypeRepository;
import club.dnd5.portal.repository.classes.ClassRepository;

@RestController
public class ClassApiController {
	@Autowired
	private ClassRepository classRepo;
	@Autowired
	private ArchetypeRepository archetypeRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@PostMapping("/api/v1/filters/classes")
	public FilterApi getClassFilter() {
		return getClassFilters();
	}
	
	@PostMapping(value = "/api/v1/classes", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ClassApi> getClasses(@RequestBody ClassRequestApi request) {
		if (request.getSearch() != null && request.getSearch().getValue() != null && !request.getSearch().getValue().isEmpty()) {
			return classRepo.findAll()
					.stream()
					.map(cclass -> new ClassApi(cclass, request))
					.filter(c -> !c.getArchetypes().isEmpty() || !c.getIcon())
					.collect(Collectors.toList());
		}
		return classRepo.findAll()
				.stream()
				.map(cclass -> new ClassApi(cclass, request))
				.collect(Collectors.toList());
	}
	
	@PostMapping(value = "/api/v1/classes/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClassInfoApiDto> getClassInfo(@PathVariable String englishName) {
		HeroClass heroClass = classRepo.findByEnglishName(englishName.replace('_', ' '));
		if (heroClass == null) {
			return ResponseEntity.notFound().build();
		}
		Collection<String> images = imageRepository.findAllByTypeAndRefId(ImageType.CLASS, heroClass.getId());
		return ResponseEntity.ok(new ClassInfoApiDto(heroClass, images));
	}
	
	@PostMapping(value = "/api/v1/classes/{className}/{archetypeName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClassInfoApiDto> getArchetypeInfo(@PathVariable String className, @PathVariable String archetypeName) {
		HeroClass heroClass = classRepo.findByEnglishName(className);
		if (heroClass == null) {
			return ResponseEntity.notFound().build();
		}
		Archetype archetype = heroClass.getArchetypes().stream().filter(a -> a.getEnglishName().equalsIgnoreCase(archetypeName.replace('_', ' '))).findFirst().get();
		Collection<String> images = imageRepository.findAllByTypeAndRefId(ImageType.SUBCLASS, heroClass.getId());
		return ResponseEntity.ok(new ClassInfoApiDto(archetype, images));
	}
	
	private FilterApi getClassFilters() {
		FilterApi filters = new FilterApi();
		List<FilterApi> classSources = new ArrayList<>();

		FilterApi mainFilter = new FilterApi("main");
		mainFilter.setValues(
				Stream.concat(classRepo.findBook(TypeBook.OFFICAL).stream(), archetypeRepository.findBook(TypeBook.OFFICAL).stream())
				.distinct()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		classSources.add(mainFilter);
		
		FilterApi settingFilter = new FilterApi("Сеттинги", "settings");
		settingFilter.setValues(
				Stream.concat(classRepo.findBook(TypeBook.SETTING).stream(), archetypeRepository.findBook(TypeBook.SETTING).stream())
				.distinct()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		classSources.add(settingFilter);

		FilterApi homebrewFilter = new FilterApi("Homebrew", "homebrew");
		homebrewFilter.setValues(
				Stream.concat(classRepo.findBook(TypeBook.CUSTOM).stream(), archetypeRepository.findBook(TypeBook.CUSTOM).stream())
				.distinct()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		classSources.add(homebrewFilter);
		
		filters.setSources(classSources);
		
		List<FilterApi> others = new ArrayList<>();
		FilterApi hillDiceFilter = new FilterApi("Кость хитов", "hitdice");
		hillDiceFilter.setValues(
				EnumSet.of(Dice.d6, Dice.d8, Dice.d10, Dice.d12).stream()
				.map(dice -> new FilterValueApi(dice.getName(), dice.name()))
				.collect(Collectors.toList())
		);
		others.add(hillDiceFilter);
		filters.setOther(others);
		return filters;
	}
}