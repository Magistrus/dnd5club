package club.dnd5.portal.controller.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.FilterApi;
import club.dnd5.portal.dto.api.FilterValueApi;
import club.dnd5.portal.dto.api.FltersApi;
import club.dnd5.portal.model.Dice;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.splells.MagicSchool;
import club.dnd5.portal.repository.classes.ArchetypeRepository;
import club.dnd5.portal.repository.classes.ClassRepository;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;

@RestController
public class FilterApiController {
	private static final String[][] classesMap = { { "1", "Бард" }, { "2", "Волшебник" }, { "3", "Друид" },
			{ "4", "Жрец" }, { "5", "Колдун" }, { "6", "Паладин" }, { "7", "Следопыт" }, { "8", "Чародей" },
			{ "14", "Изобретатель" } };
	
	@Autowired
	private ClassRepository classRepository;
	@Autowired
	private ArchetypeRepository archetypeRepository;
	@Autowired
	private SpellDatatableRepository spellRepository;
	
	@GetMapping("/api/v1/filters")
	public FltersApi getFilters() {
		FltersApi filters = new FltersApi();
		// фильтры классов
		FilterApi classFilter = new FilterApi();
		List<FilterApi> classSources = new ArrayList<>();

		FilterApi classMainFilter = new FilterApi("main");
		classMainFilter.setValues(
				Stream.concat(classRepository.findBook(TypeBook.OFFICAL).stream(), archetypeRepository.findBook(TypeBook.OFFICAL).stream())
				.distinct()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		classSources.add(classMainFilter);
		
		FilterApi classSettingFilter = new FilterApi("Сеттинги", "settings");
		classSettingFilter.setValues(
				Stream.concat(classRepository.findBook(TypeBook.SETTING).stream(), archetypeRepository.findBook(TypeBook.SETTING).stream())
				.distinct()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		classSources.add(classSettingFilter);

		FilterApi classHomebrewFilter = new FilterApi("Homebrew", "homebrew");
		classHomebrewFilter.setValues(
				Stream.concat(classRepository.findBook(TypeBook.CUSTOM).stream(), archetypeRepository.findBook(TypeBook.CUSTOM).stream())
				.distinct()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		classSources.add(classHomebrewFilter);
		
		classFilter.setSources(classSources);
		
		List<FilterApi> others = new ArrayList<>();
		FilterApi hillDiceFilter = new FilterApi("Кость хитов", "hitdice");
		hillDiceFilter.setValues(
				EnumSet.of(Dice.d6, Dice.d8, Dice.d10, Dice.d12).stream()
				.map(dice -> new FilterValueApi(dice.getName(), dice.name(), Boolean.TRUE))
				.collect(Collectors.toList())
		);
		others.add(hillDiceFilter);
		classFilter.setOther(others);
		filters.setClasses(classFilter);
		
		// фильтры заклинаний
		FilterApi spellFilters = new FilterApi();
		List<FilterApi> spellSources = new ArrayList<>();
		FilterApi spellMainFilter = new FilterApi("main");
		spellMainFilter.setValues(
				spellRepository.findBook(TypeBook.OFFICAL).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		spellSources.add(spellMainFilter);
		
		FilterApi spellSettingFilter = new FilterApi("Сеттинги", "settings");
		spellSettingFilter.setValues(
				spellRepository.findBook(TypeBook.SETTING).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		spellSources.add(spellSettingFilter);
		
		FilterApi spellAdventureFilter = new FilterApi("Приключения", "adventures");
		spellAdventureFilter.setValues(
				spellRepository.findBook(TypeBook.MODULE).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		spellSources.add(spellAdventureFilter);
		
		FilterApi spellHomebrewFilter = new FilterApi("Homebrew", "homebrew");
		spellHomebrewFilter.setValues(
				spellRepository.findBook(TypeBook.CUSTOM).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		spellSources.add(spellHomebrewFilter);
		spellFilters.setSources(spellSources);
		
		List<FilterApi> spellOtherFilters = new ArrayList<>();
		FilterApi levelFilter = new FilterApi("Уровень", "level");
		levelFilter.setValues(IntStream.rangeClosed(0, 9)
				 .mapToObj(level -> new FilterValueApi(level == 0 ? "заговор" : String.valueOf(level),  String.valueOf(level), Boolean.TRUE))
				 .collect(Collectors.toList()));
		spellOtherFilters.add(levelFilter);
		

		FilterApi spellClassFilter = new FilterApi("Классы", "class");
		spellClassFilter.setValues(IntStream.range(0, classesMap.length)
				 .mapToObj(indexSpellClass -> new FilterValueApi(classesMap[indexSpellClass][1], classesMap[indexSpellClass][0], Boolean.TRUE))
				 .collect(Collectors.toList()));
		spellOtherFilters.add(spellClassFilter);
		
		FilterApi schoolSpellFilter = new FilterApi("Классы", "school");
		schoolSpellFilter.setValues(
				Arrays.stream(MagicSchool.values())
				 .map(school -> new FilterValueApi(school.getName(), school.name(), Boolean.TRUE))
				 .collect(Collectors.toList()));
		spellOtherFilters.add(schoolSpellFilter);
		
		spellFilters.setOther(spellOtherFilters);
		
		filters.setSpells(spellFilters);
		/*
		 * FilterApi classSettingFilter = new FilterApi();
		 * classSettingFilter.setValues(classRepository.findBook(TypeBook.OFFICAL)
		 * .stream() .map(book -> new FilterValueApi(book.getSource(), "main",
		 * Boolean.TRUE, book.getName())) .collect(Collectors.toList()));
		 * classFilters.add(classSettingFilter);
		 * 
		 * filters.setClasses(classFilters);
		 * 
		 * List<FilterApi> spellFilters = new ArrayList<>(); FilterApi levelFilter = new
		 * FilterApi("Уровень"); levelFilter.setValues(IntStream.rangeClosed(0, 9)
		 * .mapToObj(level -> new FilterValueApi(String.valueOf(level), "level",
		 * Boolean.TRUE, String.valueOf(level))) .collect(Collectors.toList()));
		 * spellFilters.add(levelFilter); filters.setSpells(spellFilters);
		 */
		return filters;
	}
}
