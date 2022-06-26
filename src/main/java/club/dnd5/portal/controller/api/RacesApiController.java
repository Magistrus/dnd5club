package club.dnd5.portal.controller.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.Search;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.FilterApi;
import club.dnd5.portal.dto.api.FilterValueApi;
import club.dnd5.portal.dto.api.classes.RaceRequestApi;
import club.dnd5.portal.dto.api.races.RaceApi;
import club.dnd5.portal.dto.api.races.RaceDetailApi;
import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.model.races.Race;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.datatable.RaceDataRepository;
import club.dnd5.portal.util.SpecificationUtil;

@RestController
public class RacesApiController {
	@Autowired
	private RaceDataRepository raceRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@PostMapping("/api/v1/filters/races")
	public FilterApi getRacesFilter() {
		FilterApi filters = new FilterApi();
		List<FilterApi> sources = new ArrayList<>();
		FilterApi mainFilter = new FilterApi("main");
		mainFilter.setValues(
				raceRepository.findBook(TypeBook.OFFICAL).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(mainFilter);
		
		FilterApi settingFilter = new FilterApi("Сеттинги", "settings");
		settingFilter.setValues(
				raceRepository.findBook(TypeBook.SETTING).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(settingFilter);
		
		FilterApi adventureFilter = new FilterApi("Приключения", "adventures");
		adventureFilter.setValues(
				raceRepository.findBook(TypeBook.MODULE).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(adventureFilter);
		
		FilterApi homebrewFilter = new FilterApi("Homebrew", "homebrew");
		homebrewFilter.setValues(
				raceRepository.findBook(TypeBook.CUSTOM).stream()
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
	public List<RaceApi> getRaces(@RequestBody RaceRequestApi request) {
		Specification<Race> specification = null;

		DataTablesInput input = new DataTablesInput();
		List<Column> columns = new ArrayList<Column>(3);
		Column column = new Column();
		column.setData("name");
		column.setName("name");
		column.setSearchable(Boolean.TRUE);
		column.setOrderable(Boolean.TRUE);
		column.setSearch(new Search("", Boolean.FALSE));
		columns.add(column);
		
		column = new Column();
		column.setData("englishName");
		column.setName("englishName");
		column.setSearch(new Search("", Boolean.FALSE));
		column.setSearchable(Boolean.TRUE);
		column.setOrderable(Boolean.TRUE);
		columns.add(column);
		
		column = new Column();
		column.setData("altName");
		column.setName("altName");
		column.setSearchable(Boolean.TRUE);
		column.setOrderable(Boolean.FALSE);

		columns.add(column);
		if (request.getOrders()!=null && !request.getOrders().isEmpty()) {
			
			specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> {
				List<Order> orders = request.getOrders().stream()
						.map(
							order -> "asc".equals(order.getDirection()) ? cb.asc(root.get(order.getField())) : cb.desc(root.get(order.getField()))
						)
						.collect(Collectors.toList());
				query.orderBy(orders);
				return cb.and();
			});
		}
		input.setColumns(columns);
		input.setLength(request.getLimit() != null ? request.getLimit() : -1);
		if (request.getPage() != null && request.getLimit()!=null) {
			input.setStart(request.getPage() * request.getLimit());	
		}
		if (!request.getFilter().getBooks().isEmpty()) {
			specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> {
				Join<Book, Race> join = root.join("book", JoinType.INNER);
				return join.get("source").in(request.getFilter().getBooks());
			});
		}
		if (request.getSearch() != null) {
			if (request.getSearch().getValue() != null && !request.getSearch().getValue().isEmpty()) {
				if (request.getSearch().getExact() != null && request.getSearch().getExact()) {
					specification = (root, query, cb) -> cb.equal(root.get("name"), request.getSearch().getValue().trim().toUpperCase());
				} else {
					input.getSearch().setValue(request.getSearch().getValue());
					input.getSearch().setRegex(Boolean.FALSE);
				}
			}
						return raceRepository.findAll(input, specification, specification, RaceApi::new).getData();
		} else {
			return raceRepository.findAllByParent(null, Sort.by("name")).stream().map(RaceApi::new).collect(Collectors.toList());	
		}
	}
	
	@PostMapping(value = "/api/v1/races/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RaceDetailApi> getRace(@PathVariable String englishName) {
		Optional<Race> race = raceRepository.findByEnglishName(englishName.replace('_', ' '));
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
		Optional<Race> race = raceRepository.findBySubrace(englishRaceName.replace('_', ' '), englishSubraceName.replace('_', ' '));
		RaceDetailApi raceApi = new RaceDetailApi(race.get());
		Collection<String> images = imageRepository.findAllByTypeAndRefId(ImageType.RACE, race.get().getId());
		if (!images.isEmpty()) {
			raceApi.setImages(images);
		}
		return raceApi;
	}
}