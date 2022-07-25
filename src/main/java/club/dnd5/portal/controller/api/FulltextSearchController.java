package club.dnd5.portal.controller.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.SearchApi;
import club.dnd5.portal.repository.classes.ClassRepository;
import club.dnd5.portal.repository.classes.RaceRepository;

@RestController
@RequestMapping("/api/v1/search")
public class FulltextSearchController {
	@Autowired
	ClassRepository classRepository;
	@Autowired
	RaceRepository racrRepository;
	
	@PostMapping
	public Collection<SearchApi> search(String search){
		Collection<SearchApi> result = new ArrayList<>();
		result.addAll(racrRepository.findByEnglishNameContainsOrNameContainsOrAltNameContains(search, search, search)
				.stream()
				.map(SearchApi::new)
				.collect(Collectors.toList()));
		return result;
	}
}
