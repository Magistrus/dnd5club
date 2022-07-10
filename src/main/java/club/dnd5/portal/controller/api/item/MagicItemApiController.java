package club.dnd5.portal.controller.api.item;

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
import club.dnd5.portal.dto.api.item.MagicItemApi;
import club.dnd5.portal.dto.api.item.MagicItemDetailApi;
import club.dnd5.portal.dto.api.item.MagicItemRequesApi;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.model.items.MagicItem;
import club.dnd5.portal.model.items.MagicThingType;
import club.dnd5.portal.model.items.Rarity;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.datatable.MagicItemDatatableRepository;
import club.dnd5.portal.util.SpecificationUtil;

@RestController
public class MagicItemApiController {
	@Autowired
	private MagicItemDatatableRepository magicItemRepository;
	@Autowired
	private ImageRepository imageRepo;

	@PostMapping(value = "/api/v1/items/magic", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<MagicItemApi> getItems(@RequestBody MagicItemRequesApi request) {
		Specification<MagicItem> specification = null;

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

		input.setColumns(columns);
		input.setLength(request.getLimit() != null ? request.getLimit() : -1);
		if (request.getPage() != null && request.getLimit()!=null) {
			input.setStart(request.getPage() * request.getLimit());	
		}
		if (request.getSearch() != null) {
			if (request.getSearch().getValue() != null && !request.getSearch().getValue().isEmpty()) {
				if (request.getSearch().getExact() != null && request.getSearch().getExact()) {
					specification = (root, query, cb) -> cb.equal(root.get("name"),
							request.getSearch().getValue().trim().toUpperCase());
				} else {
					input.getSearch().setValue(request.getSearch().getValue());
					input.getSearch().setRegex(Boolean.FALSE);
				}
			}
		}
		if (request.getFilter() != null) {
			if (!request.getFilter().getBooks().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> {
					Join<Book, Spell> join = root.join("book", JoinType.INNER);
					return join.get("source").in(request.getFilter().getBooks());
				});
			}
			if (!request.getFilter().getRarity().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification, 
					(root, query, cb) -> root.get("rarity").in(request.getFilter().getRarity().stream().map(Rarity::valueOf).collect(Collectors.toList())));
			}
			if (!request.getFilter().getType().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification, 
					(root, query, cb) -> root.get("type").in(request.getFilter().getType().stream().map(MagicThingType::valueOf).collect(Collectors.toList())));
			}
			if (!request.getFilter().getCustomization().isEmpty()) {
				if (request.getFilter().getCustomization().contains("1")) {
					specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> cb.equal(root.get("customization"), 1));
				} 
				if (request.getFilter().getCustomization().contains("2")) {
					specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> cb.equal(root.get("customization"), 0));
				}
			}
			if (!request.getFilter().getConsumable().isEmpty()) {
				if (request.getFilter().getConsumable().contains("1")) {
					specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> cb.equal(root.get("consumed"), 1));
				} 
				if (request.getFilter().getConsumable().contains("2")) {
					specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> cb.equal(root.get("consumed"), 0));
				}
			}
			if (!request.getFilter().getCharge().isEmpty()) {
				if (request.getFilter().getCharge().contains("1")) {
					specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> cb.isNotNull(root.get("charge")));
				} 
				if (request.getFilter().getCharge().contains("2")) {
					specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> cb.isNull(root.get("charge")));
				}
			}
		}
		if (request.getOrders() != null && !request.getOrders().isEmpty()) {
			specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> {
				List<Order> orders = request.getOrders().stream()
						.map(order -> "asc".equals(order.getDirection()) ? cb.asc(root.get(order.getField()))
								: cb.desc(root.get(order.getField())))
						.collect(Collectors.toList());
				query.orderBy(orders);
				return cb.and();
			});
		}
		return magicItemRepository.findAll(input, specification, specification, MagicItemApi::new).getData();
	}

	@PostMapping(value = "/api/v1/items/magic/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public MagicItemDetailApi getItem(@PathVariable String englishName) {
		MagicItem item = magicItemRepository.findByEnglishName(englishName.replace('_', ' '));
		MagicItemDetailApi itemApi = new MagicItemDetailApi(item);
		Collection<String> images = imageRepo.findAllByTypeAndRefId(ImageType.MAGIC_ITEM, item.getId());
		if (!images.isEmpty()) {
			itemApi.setImages(images);
		}
		return itemApi;
	}
	
	@PostMapping("/api/v1/filters/items/magic")
	public FilterApi getMagicItemsFilter() {
		FilterApi filters = new FilterApi();
		List<FilterApi> sources = new ArrayList<>();
		FilterApi spellMainFilter = new FilterApi("main");
		spellMainFilter.setValues(
				magicItemRepository.findBook(TypeBook.OFFICAL).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(spellMainFilter);
		
		FilterApi settingFilter = new FilterApi("Сеттинги", "settings");
		settingFilter.setValues(
				magicItemRepository.findBook(TypeBook.SETTING).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(settingFilter);
		
		FilterApi adventureFilter = new FilterApi("Приключения", "adventures");
		adventureFilter.setValues(
				magicItemRepository.findBook(TypeBook.MODULE).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(adventureFilter);
		
		FilterApi homebrewFilter = new FilterApi("Homebrew", "homebrew");
		homebrewFilter.setValues(
				magicItemRepository.findBook(TypeBook.CUSTOM).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(homebrewFilter);
		filters.setSources(sources);
		
		List<FilterApi> otherFilters = new ArrayList<>();
		
		FilterApi rarityFilter = new FilterApi("Редкость", "rarity");
		rarityFilter.setValues(
				Arrays.stream(Rarity.values())
				 .map(value -> new FilterValueApi(value.getNames()[1], value.name()))
				 .collect(Collectors.toList()));
		otherFilters.add(rarityFilter);

		FilterApi typeFilter = new FilterApi("Тип предмета", "type");
		typeFilter.setValues(
				Arrays.stream(MagicThingType.values())
				 .map(value -> new FilterValueApi(value.getCyrilicName(), value.name()))
				 .collect(Collectors.toList()));
		otherFilters.add(typeFilter);
		
		FilterApi attumentFilter = new FilterApi("Настройка", "customization");
		List<FilterValueApi> values = new ArrayList<>(2);
		values.add(new FilterValueApi("требуется", 1));
		values.add(new FilterValueApi("не требуется", 2));
		attumentFilter.setValues(values);
		otherFilters.add(attumentFilter);
		
		FilterApi consumableFilter = new FilterApi("Расходуемый при использовании", "consumable");
		values = new ArrayList<>(2);
		values.add(new FilterValueApi("да", 1));
		values.add(new FilterValueApi("нет", 2));
		consumableFilter.setValues(values);
		otherFilters.add(consumableFilter);
		
		FilterApi chargeFilter = new FilterApi("Есть заряды", "charge");
		values = new ArrayList<>(2);
		values.add(new FilterValueApi("да", 1));
		values.add(new FilterValueApi("нет", 2));
		chargeFilter.setValues(values);
		otherFilters.add(chargeFilter);
		
		filters.setOther(otherFilters);
		return filters;
	}
}