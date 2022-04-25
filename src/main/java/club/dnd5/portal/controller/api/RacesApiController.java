package club.dnd5.portal.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.classes.RaceApiDto;
import club.dnd5.portal.repository.classes.RaceRepository;

@RestController
public class RacesApiController {
	@Autowired
	private RaceRepository raceRepo;
	
	@GetMapping(value = "/api/v1/races", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RaceApiDto> getClasses() {
		return raceRepo.findAllByParent(null, Sort.by("name")).stream().map(RaceApiDto::new).collect(Collectors.toList());
	}
}