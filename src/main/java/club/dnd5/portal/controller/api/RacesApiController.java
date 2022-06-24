package club.dnd5.portal.controller.api;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.races.RaceApi;
import club.dnd5.portal.dto.api.races.RaceDetailApi;
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
	
	@PostMapping(value = "/api/v1/races", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RaceApi> getRaces() {
		return raceRepo.findAllByParent(null, Sort.by("name")).stream().map(RaceApi::new).collect(Collectors.toList());
	}
	
	@PostMapping(value = "/api/v1/races/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RaceDetailApi getRace(@PathVariable String englishName) {
		Optional<Race> race = raceRepo.findByEnglishName(englishName.replace('_', ' '));
		RaceDetailApi raceApi = new RaceDetailApi(race.get());
		Collection<String> images = imageRepository.findAllByTypeAndRefId(ImageType.RACE, race.get().getId());
		if (!images.isEmpty()) {
			raceApi.setImages(images);
		}
		return raceApi;
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