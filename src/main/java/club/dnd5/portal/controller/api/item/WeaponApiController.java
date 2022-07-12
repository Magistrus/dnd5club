package club.dnd5.portal.controller.api.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import club.dnd5.portal.dto.api.item.WeaponApi;
import club.dnd5.portal.dto.api.item.WeaponDetailApi;
import club.dnd5.portal.dto.api.item.WeaponRequesApi;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.Dice;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.items.Weapon;
import club.dnd5.portal.model.items.WeaponProperty;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.datatable.WeaponDatatableRepository;
import club.dnd5.portal.repository.datatable.WeaponPropertyDatatableRepository;
import club.dnd5.portal.util.SpecificationUtil;

@RestController
public class WeaponApiController {
	@Autowired
	private WeaponDatatableRepository weaponRepository;
	@Autowired
	private WeaponPropertyDatatableRepository propertyRepository;
	
	@PostMapping(value = "/api/v1/weapons", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<WeaponApi> getWeapon(@RequestBody WeaponRequesApi request) {
		Specification<Weapon> specification = null;

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
					Join<Book, Spell> join = root.join("book", JoinType.INNER);
					return join.get("source").in(request.getFilter().getBooks());
				});
			}
			if (!request.getFilter().getDamageType().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification,
						(root, query, cb) -> root.get("damageType").in(request.getFilter().getDamageType()));
			}
			if (!request.getFilter().getProperrty().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> {
					Join<WeaponProperty, Weapon> join = root.join("properties", JoinType.LEFT);
					query.distinct(true);
					return cb.and(join.get("id").in(request.getFilter().getProperrty()));
				});
			}
			if (!request.getFilter().getDice().isEmpty()) {

				Set<Integer> damages = new HashSet<>(2);
				List<Dice> filterDamageDices = request.getFilter().getDice().stream()
						.filter(s -> !s.isEmpty())
						.map(d -> {
							if(d.startsWith("2")) {
								damages.add(2);
								return d.replace("2", "");
							} 
							return d;
						})
						.map(Dice::valueOf)
						.collect(Collectors.toList());
				if (!filterDamageDices.isEmpty()) {
					specification = SpecificationUtil.getAndSpecification(specification,
							(root, query, cb) -> root.get("damageDice").in(filterDamageDices));
				}
				if (damages.contains(2)) {
					specification = SpecificationUtil.getAndSpecification(specification,
							(root, query, cb) -> root.get("numberDice").in(2));
				}
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
		return weaponRepository.findAll(input, specification, specification, WeaponApi::new).getData();
	}
	
	@PostMapping(value = "/api/v1/weapons/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WeaponDetailApi getWeapon(@PathVariable String englishName) {
		return new WeaponDetailApi(weaponRepository.findByEnglishName(englishName.replace('_', ' ')));
	}
	
	@PostMapping("/api/v1/filters/weapons")
	public FilterApi getWeaponsFilter() {
		FilterApi filters = new FilterApi();
		List<FilterApi> sources = new ArrayList<>();
		FilterApi spellMainFilter = new FilterApi("main");
		spellMainFilter.setValues(
				weaponRepository.findBook(TypeBook.OFFICAL).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(spellMainFilter);
		
		FilterApi settingFilter = new FilterApi("Сеттинги", "settings");
		settingFilter.setValues(
				weaponRepository.findBook(TypeBook.SETTING).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(settingFilter);
		
		FilterApi adventureFilter = new FilterApi("Приключения", "adventures");
		adventureFilter.setValues(
				weaponRepository.findBook(TypeBook.MODULE).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(adventureFilter);
		
		FilterApi homebrewFilter = new FilterApi("Homebrew", "homebrew");
		homebrewFilter.setValues(
				weaponRepository.findBook(TypeBook.CUSTOM).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(homebrewFilter);
		filters.setSources(sources);
		
		List<FilterApi> otherFilters = new ArrayList<>();
		
		FilterApi damageTypeFilter = new FilterApi("По типу урона", "damageType");
		damageTypeFilter.setValues(
				DamageType.getWeaponDamage().stream()
				 .map(value -> new FilterValueApi(value.getCyrilicName(), value.name()))
				 .collect(Collectors.toList()));
		otherFilters.add(damageTypeFilter);

		FilterApi properetyTypeFilter = new FilterApi("По свойствам", "properrty");
		properetyTypeFilter.setValues(
				propertyRepository.findAll().stream()
				 .map(value -> new FilterValueApi(value.getName(), value.getId()))
				 .collect(Collectors.toList()));
		otherFilters.add(properetyTypeFilter);

		FilterApi diceFilter = new FilterApi("По кости урона", "dice");
		diceFilter.setValues(
				Arrays.stream(new String[]{"к4", "2к4", "к6", "2к6", "к8", "к10", "к12"})
				 .map(value -> new FilterValueApi(value, value.replace('к', 'd')))
				 .collect(Collectors.toList()));
		otherFilters.add(diceFilter);
		
		filters.setOther(otherFilters);
		return filters;
	}
}