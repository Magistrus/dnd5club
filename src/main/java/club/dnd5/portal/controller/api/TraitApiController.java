package club.dnd5.portal.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;

import org.springframework.beans.factory.annotation.Autowired;
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
import club.dnd5.portal.dto.api.classes.TraitApi;
import club.dnd5.portal.dto.api.classes.TraitDetailApi;
import club.dnd5.portal.dto.api.classes.TraitRequesApi;
import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.trait.Trait;
import club.dnd5.portal.repository.datatable.TraitDatatableRepository;
import club.dnd5.portal.util.SpecificationUtil;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Trait", description = "The Trait API")
@RestController
public class TraitApiController {
	@Autowired
	private TraitDatatableRepository traitRepository;
	
	@PostMapping(value = "/api/v1/traits", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TraitApi> getTraits(@RequestBody TraitRequesApi request) {
		Specification<Trait> specification = null;

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
		
		input.setColumns(columns);
		input.setLength(request.getLimit() != null ? request.getLimit() : -1);
		if (request.getPage() != null && request.getLimit()!=null) {
			input.setStart(request.getPage() * request.getLimit());	
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
		}
		if (request.getFilter() != null) {
			if (!request.getFilter().getBooks().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> {
					Join<Book, Trait> join = root.join("book", JoinType.INNER);
					return join.get("source").in(request.getFilter().getBooks());
				});
			}
			if (!request.getFilter().getAbilities().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> {
					Join<AbilityType, Trait> join = root.join("abilities", JoinType.LEFT);
					query.distinct(true);
					return cb.and(join.in(request.getFilter().getAbilities().stream().map(AbilityType::valueOf).collect(Collectors.toList())));
				});
			}
		}
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
		return traitRepository.findAll(input, specification, specification, TraitApi::new).getData();
	}
	
	@PostMapping(value = "/api/v1/traits/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TraitDetailApi> getTrait(@PathVariable String englishName) {
		Trait trait = traitRepository.findByEnglishName(englishName.replace('_', ' '));
		if (trait == null) {
			return ResponseEntity.notFound().build(); 
		}
		return ResponseEntity.ok(new TraitDetailApi(trait));
	}

	@PostMapping("/api/v1/filters/traits")
	public FilterApi getTraitFilter() {
		FilterApi filters = new FilterApi();
		List<FilterApi> sources = new ArrayList<>();
		FilterApi spellMainFilter = new FilterApi("main");
		spellMainFilter.setValues(
				traitRepository.findBook(TypeBook.OFFICAL).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(spellMainFilter);
		
		FilterApi settingFilter = new FilterApi("Сеттинги", "settings");
		settingFilter.setValues(
				traitRepository.findBook(TypeBook.SETTING).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(settingFilter);
		
		FilterApi adventureFilter = new FilterApi("Приключения", "adventures");
		adventureFilter.setValues(
				traitRepository.findBook(TypeBook.MODULE).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(adventureFilter);
		
		FilterApi homebrewFilter = new FilterApi("Homebrew", "homebrew");
		homebrewFilter.setValues(
				traitRepository.findBook(TypeBook.CUSTOM).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(homebrewFilter);
		filters.setSources(sources);
		
		List<FilterApi> otherFilters = new ArrayList<>();
		FilterApi abilitiesFilter = new FilterApi("Характеристики", "abilities");
		abilitiesFilter.setValues(
				AbilityType.getBaseAbility().stream()
				 .map(ability -> new FilterValueApi(ability.getCyrilicName(), ability.name()))
				 .collect(Collectors.toList()));
		
		otherFilters.add(abilitiesFilter);
		filters.setOther(otherFilters);
		return filters;
	}
}