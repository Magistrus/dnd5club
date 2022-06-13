package club.dnd5.portal.controller.api.bestiary;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.bestiary.BeastApi;
import club.dnd5.portal.dto.api.bestiary.BeastDetailApi;
import club.dnd5.portal.dto.api.bestiary.BeastlRequesApi;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.foundary.FBeastiary;
import club.dnd5.portal.model.fvtt.plutonium.FBeast;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.datatable.BestiaryDatatableRepository;

@RestController
public class BeastApiConroller {
	@Autowired
	private BestiaryDatatableRepository repo;

	@Autowired
	private ImageRepository imageRepo;

	@PostMapping(value = "/api/v1/bestiary", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BeastApi> getBestiary(@RequestBody BeastlRequesApi request) {
		Specification<Creature> specification = null;

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
			
			specification = addSpecification(specification, (root, query, cb) -> {
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
			if (!request.getFilter().getChallengeRatings().isEmpty()) {
				specification = addSpecification(specification, (root, query, cb) -> root.get("challengeRating").in(request.getFilter().getChallengeRatings()));
			}
			if (!request.getFilter().getBooks().isEmpty()) {
				specification = addSpecification(specification, (root, query, cb) -> {
					Join<Book, Spell> join = root.join("book", JoinType.INNER);
					return join.get("source").in(request.getFilter().getBooks());
				});
			}
		}
		return repo.findAll(input, specification, specification, BeastApi::new).getData();
	}
	
	@PostMapping(value = "/api/v1/bestiary/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BeastDetailApi getBeast(@PathVariable String englishName) {
		Creature beast = repo.findByEnglishName(englishName.replace('_', ' '));
		BeastDetailApi beastApi = new BeastDetailApi(beast);
		Collection<String> images = imageRepo.findAllByTypeAndRefId(ImageType.CREATURE, beast.getId());
		if (!images.isEmpty()) {
			beastApi.setImages(images);
		}
		return beastApi;
	}
	
	@CrossOrigin
	@GetMapping("/api/fvtt/v1/bestiary")
	public FBeastiary getCreatures(){
		List<FBeast> list = ((Collection<Creature>) repo.findAll()).stream().map(FBeast::new).collect(Collectors.toList());
		return new FBeastiary(list);
	}
	
	private <T> Specification<T> addSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
}