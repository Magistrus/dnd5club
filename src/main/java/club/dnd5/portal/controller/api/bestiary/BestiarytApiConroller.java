package club.dnd5.portal.controller.api.bestiary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.Search;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.FilterApi;
import club.dnd5.portal.dto.api.FilterValueApi;
import club.dnd5.portal.dto.api.bestiary.BeastApi;
import club.dnd5.portal.dto.api.bestiary.BeastDetailApi;
import club.dnd5.portal.dto.api.bestiary.BeastlRequesApi;
import club.dnd5.portal.model.CreatureSize;
import club.dnd5.portal.model.CreatureType;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.creature.Action;
import club.dnd5.portal.model.creature.ActionType;
import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.creature.CreatureFeat;
import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.foundary.FBeastiary;
import club.dnd5.portal.model.foundary.FCreature;
import club.dnd5.portal.model.fvtt.plutonium.FBeast;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.datatable.BestiaryDatatableRepository;
import club.dnd5.portal.repository.datatable.TagBestiaryDatatableRepository;
import club.dnd5.portal.util.SpecificationUtil;

@RestController
public class BestiarytApiConroller {
	@Autowired
	private BestiaryDatatableRepository beastRepository;
	
	@Autowired
	private TagBestiaryDatatableRepository tagRepository;
	
	@Autowired
	private ImageRepository imageRepository;

	@PostMapping(value = "/api/v1/bestiary", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BeastApi> getBestiary(@RequestBody BeastlRequesApi request) {
		Specification<Creature> specification = null;

		DataTablesInput input = new DataTablesInput();
		List<Column> columns = new ArrayList<>(3);
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
			if (request.getFilter().getNpc().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> cb.notEqual(root.get("raceId"), 102));
			}
			if (!request.getFilter().getChallengeRatings().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> root.get("challengeRating").in(request.getFilter().getChallengeRatings()));
			}
			if (!request.getFilter().getBooks().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> {
					Join<Book, Spell> join = root.join("book", JoinType.INNER);
					return join.get("source").in(request.getFilter().getBooks());
				});
			}
			if (!request.getFilter().getTypes().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(
						specification,  (root, query, cb) -> root.get("type").in(request.getFilter().getTypes()));
			}
			if (!request.getFilter().getSizes().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(
						specification,  (root, query, cb) -> root.get("size").in(request.getFilter().getSizes()));
			}
			if (!request.getFilter().getTags().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification,  (root, query, cb) -> {
					Join<Object, Object> join = root.join("races", JoinType.INNER);
					query.distinct(true);
					return cb.and(join.get("id").in(request.getFilter().getTags()));
				});
			}
			if (!request.getFilter().getMoving().isEmpty()) {
				if(request.getFilter().getMoving().contains("fly")) {
					specification = SpecificationUtil.getAndSpecification(specification,
							(root, query, cb) -> cb.isNotNull(root.get("flySpeed")));
				}
				if(request.getFilter().getMoving().contains("hover")) {
					specification = SpecificationUtil.getAndSpecification(specification,
							(root, query, cb) -> cb.isNotNull(root.get("hover")));
				}
				if(request.getFilter().getMoving().contains("climbs")) {
					specification = SpecificationUtil.getAndSpecification(specification,
							(root, query, cb) -> cb.isNotNull(root.get("climbingSpeed")));
				}
				if(request.getFilter().getMoving().contains("swim")) {
					specification = SpecificationUtil.getAndSpecification(specification,
							(root, query, cb) -> cb.isNotNull(root.get("swimmingSpped")));
				}
				if(request.getFilter().getMoving().contains("digger")) {
					specification = SpecificationUtil.getAndSpecification(specification,
							(root, query, cb) -> cb.isNotNull(root.get("diggingSpeed")));
				}
			}
			if (!request.getFilter().getEnvironments().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification,  (root, query, cb) -> {
					Join<Object, Object> join = root.join("habitates", JoinType.INNER);
					query.distinct(true);
					return join.in(request.getFilter().getEnvironments());
				});
			}
			if (!request.getFilter().getSenses().isEmpty()) {
				if(request.getFilter().getSenses().contains("darkvision")) {
					specification = SpecificationUtil.getAndSpecification(specification,
							(root, query, cb) -> cb.isNotNull(root.get("darkvision")));
				}
				if(request.getFilter().getSenses().contains("trysight")) {
					specification = SpecificationUtil.getAndSpecification(specification,
							(root, query, cb) -> cb.isNotNull(root.get("trysight")));
				}
				if(request.getFilter().getSenses().contains("blindsight")) {
					specification = SpecificationUtil.getAndSpecification(specification,
							(root, query, cb) -> cb.isNotNull(root.get("blindsight")));
				}
				if(request.getFilter().getSenses().contains("tremmor")) {
					specification = SpecificationUtil.getAndSpecification(specification,
							(root, query, cb) -> cb.isNotNull(root.get("vibration")));
				}
			}
			if (!CollectionUtils.isEmpty(request.getFilter().getFeatures())) {
				Specification<Creature> addSpec = null;
				if (request.getFilter().getFeatures().contains("lair")) {
					addSpec = SpecificationUtil.getOrSpecification(addSpec,  (root, query, cb) ->
						cb.isNotNull(root.get("lair"))
					);
					request.getFilter().getFeatures().remove("lair");
				}
				if (request.getFilter().getFeatures().contains("legendary")) {
					addSpec = SpecificationUtil.getOrSpecification(addSpec,  (root, query, cb) -> {
						Join<Action, Creature> join = root.join("actions", JoinType.INNER);
						query.distinct(true);
						return cb.equal(join.get("actionType"), ActionType.LEGENDARY);
					});	
					request.getFilter().getFeatures().remove("legendary");
				}
				for (String featureName : request.getFilter().getFeatures()) {
					addSpec = SpecificationUtil.getOrSpecification(addSpec,  (root, query, cb) -> {
						Join<CreatureFeat, Creature> join = root.join("feats", JoinType.INNER);
						query.distinct(true);
						return cb.like(join.get("name"), "%" + featureName + "%");
					});	
				}
				specification = SpecificationUtil.getAndSpecification(specification, addSpec); 
			}
		}
		return beastRepository.findAll(input, specification, specification, BeastApi::new).getData();
	}

	@PostMapping(value = "/api/v1/bestiary/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BeastDetailApi getBeast(@PathVariable String englishName) {
		Creature beast = beastRepository.findByEnglishName(englishName.replace('_', ' '));
		BeastDetailApi beastApi = new BeastDetailApi(beast);
		Collection<String> images = imageRepository.findAllByTypeAndRefId(ImageType.CREATURE, beast.getId());
		if (!images.isEmpty()) {
			beastApi.setImages(images);
		}
		return beastApi;
	}
	
	@GetMapping("//api/fvtt/v1/bestiary/{id}")
	public ResponseEntity<FCreature> getCreature(HttpServletResponse response, @PathVariable Integer id){
		Creature creature = beastRepository.findById(id).get();
		response.setContentType("application/json");
		String file = String.format("attachment; filename=\"%s.json\"", creature.getEnglishName());
		response.setHeader("Content-Disposition", file); 
		return ResponseEntity.ok(new FCreature(creature));
	}
	
	@CrossOrigin
	@GetMapping("/api/fvtt/v1/bestiary")
	public FBeastiary getCreatures(){
		List<FBeast> list = ((Collection<Creature>) beastRepository.findAll())
				.stream()
				.map(FBeast::new)
				.collect(Collectors.toList());
		return new FBeastiary(list);
	}
	
	@PostMapping("/api/v1/filters/bestiary")
	public FilterApi getFilter() {
		FilterApi filters = new FilterApi();
		List<FilterApi> sources = new ArrayList<>();
		FilterApi spellMainFilter = new FilterApi("main");
		spellMainFilter.setValues(
				beastRepository.findBook(TypeBook.OFFICAL).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(spellMainFilter);
		
		FilterApi settingFilter = new FilterApi("Сеттинги", "settings");
		settingFilter.setValues(
				beastRepository.findBook(TypeBook.SETTING).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(settingFilter);
		
		FilterApi adventureFilter = new FilterApi("Приключения", "adventures");
		adventureFilter.setValues(
				beastRepository.findBook(TypeBook.MODULE).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(adventureFilter);
		
		FilterApi homebrewFilter = new FilterApi("Homebrew", "homebrew");
		homebrewFilter.setValues(
				beastRepository.findBook(TypeBook.CUSTOM).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(homebrewFilter);
		filters.setSources(sources);
		
		List<FilterApi> otherFilters = new ArrayList<>();
		
		FilterApi npcFilter = new FilterApi("Именнованые НИП", "npc");
		npcFilter.setType("toggle");
		npcFilter.setValues(Collections.singletonList(new FilterValueApi("показать именованных НИП", "showNpc", Boolean.TRUE)));
		otherFilters.add(npcFilter);
		
		FilterApi crFilter = new FilterApi("Уровень опасности", "challengeRating");
		crFilter.setExpand(Boolean.TRUE);
		List<FilterValueApi> values = new ArrayList<>();
		values.add(new FilterValueApi("не определена", "undefined"));
		values.add(new FilterValueApi("0", "0"));
		values.add(new FilterValueApi("1/8", "1/8"));
		values.add(new FilterValueApi("1/4", "1/4"));
		values.add(new FilterValueApi("1/2", "1/2"));
		values.addAll(
				IntStream.rangeClosed(1, 30)
				.mapToObj(value -> new FilterValueApi(String.valueOf(value), value))
				.collect(Collectors.toList()));
		crFilter.setValues(values);
		otherFilters.add(crFilter);
		
		FilterApi typeFilter = new FilterApi("Тип существа", "type");
		typeFilter.setValues(
				CreatureType.getFilterTypes().stream()
				 .map(value -> new FilterValueApi(value.getCyrilicName(), value.name()))
				 .collect(Collectors.toList()));
		otherFilters.add(typeFilter);
		
		FilterApi sizeFilter = new FilterApi("Размер существа", "size");
		sizeFilter.setValues(
				CreatureSize.getFilterSizes().stream()
				 .map(value -> new FilterValueApi(value.getCyrilicName(), value.name()))
				 .collect(Collectors.toList()));
		otherFilters.add(sizeFilter);
		
		FilterApi tagFilter = new FilterApi("Тэги", "tag");
		tagFilter.setValues(
				tagRepository.findByOrderByName().stream()
				 .map(value -> new FilterValueApi(value.getName(), value.getId()))
				 .collect(Collectors.toList()));
		otherFilters.add(tagFilter);
		
		FilterApi moveFilter = new FilterApi("Перемещение", "moving");
		values = new ArrayList<>(3);
		values.add(new FilterValueApi("летает", "fly"));
		values.add(new FilterValueApi("парит", "hover"));
		values.add(new FilterValueApi("лазает", "climbs"));
		values.add(new FilterValueApi("плавает", "swim"));
		values.add(new FilterValueApi("копает", "digger"));
		moveFilter.setValues(values);
		otherFilters.add(moveFilter);

		FilterApi sanseFilter = new FilterApi("Чувства", "senses");
		values = new ArrayList<>(3);
		values.add(new FilterValueApi("тёмное зрение", "darkvision"));
		values.add(new FilterValueApi("истинное зрение", "trysight"));
		values.add(new FilterValueApi("слепое зрение", "blindsight"));
		values.add(new FilterValueApi("чувство вибрации", "tremmor"));
		sanseFilter.setValues(values);
		otherFilters.add(sanseFilter);

		FilterApi featureFilter = new FilterApi("Умения", "features");
		values = new ArrayList<>(3);
		values.add(new FilterValueApi("сопротивление магии", "Сопротивление магии"));
		values.add(new FilterValueApi("врождённое колдовство", "Врождённое колдовство"));
		values.add(new FilterValueApi("использование заклинаний", "Использование заклинаний"));
		values.add(new FilterValueApi("легендарное сопротивление", "Легендарное сопротивление"));
		values.add(new FilterValueApi("легендарные действия", "legendary"));
		values.add(new FilterValueApi("логово", "lair"));
		featureFilter.setValues(values);
		otherFilters.add(featureFilter);
		
		FilterApi environmentFilter = new FilterApi("Места обитания", "environment");
		environmentFilter.setValues(
				HabitatType.types().stream()
				 .map(value -> new FilterValueApi(value.getName(), value.name()))
				 .collect(Collectors.toList()));
		otherFilters.add(environmentFilter);
		
		filters.setOther(otherFilters);
		return filters;
	}
}