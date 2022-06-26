package club.dnd5.portal.controller.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.FilterApi;
import club.dnd5.portal.dto.api.FilterValueApi;
import club.dnd5.portal.dto.api.races.RaceApi;
import club.dnd5.portal.dto.api.races.RaceDetailApi;
import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.model.races.Race;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.classes.RaceRepository;

@RestController
public class RacesApiController {
	@Autowired
	private RaceRepository raceRepo;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@PostMapping("/api/v1/filters/races")
	public FilterApi getRacesFilter() {
		FilterApi filters = new FilterApi();
		List<FilterApi> sources = new ArrayList<>();
		FilterApi mainFilter = new FilterApi("main");
		mainFilter.setValues(
				raceRepo.findBook(TypeBook.OFFICAL).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(mainFilter);
		
		FilterApi settingFilter = new FilterApi("Сеттинги", "settings");
		settingFilter.setValues(
				raceRepo.findBook(TypeBook.SETTING).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(settingFilter);
		
		FilterApi adventureFilter = new FilterApi("Приключения", "adventures");
		adventureFilter.setValues(
				raceRepo.findBook(TypeBook.MODULE).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(adventureFilter);
		
		FilterApi homebrewFilter = new FilterApi("Homebrew", "homebrew");
		homebrewFilter.setValues(
				raceRepo.findBook(TypeBook.CUSTOM).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(homebrewFilter);
		filters.setSources(sources);
		
		List<FilterApi> otherFilters = new ArrayList<>();
		FilterApi levelFilter = new FilterApi("Увеличение характеристик", "abilities");
		levelFilter.setValues(
				EnumSet.of(AbilityType.STRENGTH, AbilityType.DEXTERITY,AbilityType.CONSTITUTION,AbilityType.INTELLIGENCE,AbilityType.WISDOM,AbilityType.CHARISMA)
				.stream()
				.map(value -> new FilterValueApi(value.getCyrilicName(), value.name()))
				.collect(Collectors.toList()));
		otherFilters.add(levelFilter);
		
		FilterApi skillFilter = new FilterApi("Способности", "skills");
		List<FilterValueApi> values = new ArrayList<>();
		values.add(new FilterValueApi("тёмное зрение", "darkvision"));
		values.add(new FilterValueApi("полет", "fly"));
		values.add(new FilterValueApi("плавание", "swim"));
		values.add(new FilterValueApi("лазание", "climb"));

		skillFilter.setValues(values);
		otherFilters.add(skillFilter);
		filters.setOther(otherFilters);
		return filters;
	}
	
	@PostMapping(value = "/api/v1/races", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RaceApi> getRaces() {
		return raceRepo.findAllByParent(null, Sort.by("name")).stream().map(RaceApi::new).collect(Collectors.toList());
	}
	
	@PostMapping(value = "/api/v1/races/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RaceDetailApi> getRace(@PathVariable String englishName) {
		Optional<Race> race = raceRepo.findByEnglishName(englishName.replace('_', ' '));
		if (!race.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		RaceDetailApi raceApi = new RaceDetailApi(race.get());
		Collection<String> images = imageRepository.findAllByTypeAndRefId(ImageType.RACE, race.get().getId());
		if (!images.isEmpty()) {
			raceApi.setImages(images);
		}
		return ResponseEntity.ok(raceApi);
	}
	
	@PostMapping(value = "/api/v1/races/{englishRaceName}/{englishSubraceName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RaceDetailApi getSubrace(@PathVariable String englishRaceName, @PathVariable String englishSubraceName) {
		Optional<Race> race = raceRepo.findBySubrace(englishRaceName.replace('_', ' '), englishSubraceName.replace('_', ' '));
		RaceDetailApi raceApi = new RaceDetailApi(race.get());
		Collection<String> images = imageRepository.findAllByTypeAndRefId(ImageType.RACE, race.get().getId());
		if (!images.isEmpty()) {
			raceApi.setImages(images);
		}
		return raceApi;
	}
}