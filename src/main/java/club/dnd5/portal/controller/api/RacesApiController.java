package club.dnd5.portal.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.races.RaceApi;
import club.dnd5.portal.dto.api.races.RaceDetailApi;
import club.dnd5.portal.repository.classes.RaceRepository;

@RestController
public class RacesApiController {
	@Autowired
	private RaceRepository raceRepo;
	
	@PostMapping(value = "/api/v1/races", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RaceApi> getRaces() {
		return raceRepo.findAllByParent(null, Sort.by("name")).stream().map(RaceApi::new).collect(Collectors.toList());
	}
	
	@PostMapping(value = "/api/v1/races/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RaceDetailApi getRace(@PathVariable String englishName) {
		return new RaceDetailApi(raceRepo.findByEnglishName(englishName.replace('_', '_')).get());
	}
}