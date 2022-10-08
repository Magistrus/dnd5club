package club.dnd5.portal.controller.api.wiki;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.FilterApi;
import club.dnd5.portal.dto.api.FilterValueApi;
import club.dnd5.portal.dto.api.wiki.GodApi;
import club.dnd5.portal.dto.api.wiki.GodDetailApi;
import club.dnd5.portal.dto.api.wiki.GodRequestApi;
import club.dnd5.portal.model.Alignment;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.god.Domain;
import club.dnd5.portal.model.god.God;
import club.dnd5.portal.model.god.Pantheon;
import club.dnd5.portal.model.god.Rank;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.datatable.GodDatatableRepository;
import club.dnd5.portal.repository.datatable.PantheonGodRepository;
import club.dnd5.portal.util.SpecificationUtil;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "God", description = "The God API")
@RestController
public class GodApiConroller {
	@Autowired
	private GodDatatableRepository godRepository;
	@Autowired
	private PantheonGodRepository pantheonReposotory;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@PostMapping(value = "/api/v1/gods", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GodApi> getGods(@RequestBody GodRequestApi request) {
		Specification<God> specification = null;

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
					Join<Book, God> join = root.join("book", JoinType.INNER);
					return join.get("source").in(request.getFilter().getBooks());
				});
			}
			if (!request.getFilter().getAlignment().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification,
						(root, query, cb) -> root.get("aligment").in(request.getFilter().getAlignment().stream().map(Alignment::valueOf).collect(Collectors.toList())));
			}
			if (!request.getFilter().getDomain().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> {
					Join<Domain, God> join = root.join("domains", JoinType.LEFT);
					query.distinct(true);
					return join.in(request.getFilter().getDomain().parallelStream().map(Domain::valueOf).collect(Collectors.toList()));
				});
			}
			if (!request.getFilter().getRank().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(
						specification, (root, query, cb) -> root.get("rank").in(request.getFilter().getRank().stream().map(Rank::valueOf).collect(Collectors.toList())));	
			}
				if (!request.getFilter().getPantheon().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> {
					Join<Pantheon, God> pantheon = root.join("pantheon", JoinType.LEFT);
					return cb.and(pantheon.get("id").in(request.getFilter().getPantheon()));
				});
			}
		}
		return godRepository.findAll(input, specification, specification, GodApi::new).getData();
	}
	
	@PostMapping(value = "/api/v1/gods/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public GodDetailApi getGod(@PathVariable String englishName) {
		God god = godRepository.findByEnglishName(englishName.replace('_', ' '));
		GodDetailApi godApi = new GodDetailApi(god);
		Collection<String> images = imageRepository.findAllByTypeAndRefId(ImageType.GOD, god.getId());
		if (!images.isEmpty()) {
			godApi.setImages(images);
		}
		return godApi;
	}
	
	@PostMapping("/api/v1/filters/gods")
	public FilterApi getFilter() {
		FilterApi filters = new FilterApi();
		List<FilterApi> sources = new ArrayList<>();
		for (TypeBook typeBook : TypeBook.values()) {
			List<Book> books = godRepository.findBook(typeBook);
			if (!books.isEmpty()) {
				FilterApi filter = new FilterApi(typeBook.getName(), typeBook.name());
				filter.setValues(books.stream()
						.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
						.collect(Collectors.toList()));
				sources.add(filter);
			}
		}
		filters.setSources(sources);
		
		List<FilterApi> otherFilters = new ArrayList<>();
		
		FilterApi alignmentFilter = new FilterApi("Мировоззрение", "alignment");
		alignmentFilter.setValues(
				Alignment.getGods().stream()
				 .map(value -> new FilterValueApi(value.getCyrilicName(), value.name()))
				 .collect(Collectors.toList()));
		otherFilters.add(alignmentFilter);
		
		FilterApi domainFilter = new FilterApi("Домены", "domain");
		domainFilter.setValues(
				Arrays.stream(Domain.values())
				 .map(value -> new FilterValueApi(value.getCyrilicName(), value.name()))
				 .collect(Collectors.toList()));
		otherFilters.add(domainFilter);

		
		FilterApi rankFilter = new FilterApi("Ранг", "rank");
		rankFilter.setValues(
				Arrays.stream(Rank.values())
				 .map(value -> new FilterValueApi(value.getName(), value.name()))
				 .collect(Collectors.toList()));
		otherFilters.add(rankFilter);
		
		FilterApi pantheonFilter = new FilterApi("Пантеоны", "pantheon");
		pantheonFilter.setValues(
				pantheonReposotory.findAll().stream()
				 .map(value -> new FilterValueApi(value.getName(), value.getId()))
				 .collect(Collectors.toList()));
		otherFilters.add(pantheonFilter);
		
		filters.setOther(otherFilters);
		return filters;
	}
}