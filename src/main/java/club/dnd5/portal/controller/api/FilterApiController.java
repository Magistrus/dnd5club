package club.dnd5.portal.controller.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.FilterApi;
import club.dnd5.portal.dto.api.FilterValueApi;
import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.Dice;
import club.dnd5.portal.model.SkillType;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.classes.Option;
import club.dnd5.portal.model.items.EquipmentType;
import club.dnd5.portal.model.splells.MagicSchool;
import club.dnd5.portal.repository.classes.ArchetypeRepository;
import club.dnd5.portal.repository.classes.ClassRepository;
import club.dnd5.portal.repository.classes.RaceRepository;
import club.dnd5.portal.repository.datatable.BackgroundDatatableRepository;
import club.dnd5.portal.repository.datatable.ItemDatatableRepository;
import club.dnd5.portal.repository.datatable.OptionDatatableRepository;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;
import club.dnd5.portal.repository.datatable.TraitDatatableRepository;
import club.dnd5.portal.repository.datatable.WeaponDatatableRepository;
import club.dnd5.portal.repository.datatable.WeaponPropertyDatatableRepository;

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
	private RaceRepository raceRepository;
	@Autowired
	private SpellDatatableRepository spellRepository;
	
	@Autowired
	private TraitDatatableRepository traitRepository;
	@Autowired
	private OptionDatatableRepository optionRepository;
	@Autowired
	private BackgroundDatatableRepository backgroundRepository;

	@Autowired
	private WeaponDatatableRepository weaponRepository;
	@Autowired
	private WeaponPropertyDatatableRepository propertyRepository;
	@Autowired
	private ItemDatatableRepository itemRepository;
	
	@PostMapping("/api/v1/filters/classes")
	public FilterApi getClassFilter() {
		return getClassFilters();
	}
	
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
				.map(value -> new FilterValueApi(value.getCyrilicName(), value.name(), Boolean.TRUE))
				.collect(Collectors.toList()));
		otherFilters.add(levelFilter);
		
		FilterApi skillFilter = new FilterApi("Способности", "skills");
		List<FilterValueApi> values = new ArrayList<>();
		values.add(new FilterValueApi("тёмное зрение", "darkvision", Boolean.TRUE));
		values.add(new FilterValueApi("полет", "fly", Boolean.TRUE));
		values.add(new FilterValueApi("плавание", "swim", Boolean.TRUE));
		values.add(new FilterValueApi("лазание", "climb", Boolean.TRUE));

		skillFilter.setValues(values);
		otherFilters.add(skillFilter);
		filters.setOther(otherFilters);
		return filters;
	}
	
	@PostMapping("/api/v1/filters/spells")
	public FilterApi getSpellsFilter() {
		FilterApi filters = new FilterApi();
		List<FilterApi> sources = new ArrayList<>();
		FilterApi spellMainFilter = new FilterApi("main");
		spellMainFilter.setValues(
				spellRepository.findBook(TypeBook.OFFICAL).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(spellMainFilter);
		
		FilterApi settingFilter = new FilterApi("Сеттинги", "settings");
		settingFilter.setValues(
				spellRepository.findBook(TypeBook.SETTING).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(settingFilter);
		
		FilterApi adventureFilter = new FilterApi("Приключения", "adventures");
		adventureFilter.setValues(
				spellRepository.findBook(TypeBook.MODULE).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(adventureFilter);
		
		FilterApi homebrewFilter = new FilterApi("Homebrew", "homebrew");
		homebrewFilter.setValues(
				spellRepository.findBook(TypeBook.CUSTOM).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(homebrewFilter);
		filters.setSources(sources);
		
		List<FilterApi> otherFilters = new ArrayList<>();
		
		otherFilters.add(getLevelsFilter(9));

		FilterApi spellClassFilter = new FilterApi("Классы", "class");
		spellClassFilter.setValues(IntStream.range(0, classesMap.length)
				 .mapToObj(indexSpellClass -> new FilterValueApi(classesMap[indexSpellClass][1], classesMap[indexSpellClass][0], Boolean.TRUE))
				 .collect(Collectors.toList()));
		otherFilters.add(spellClassFilter);
		
		FilterApi schoolSpellFilter = new FilterApi("Школа", "school");
		schoolSpellFilter.setValues(
				Arrays.stream(MagicSchool.values())
				 .map(school -> new FilterValueApi(school.getName(), school.name(), Boolean.TRUE))
				 .collect(Collectors.toList()));
		
		otherFilters.add(schoolSpellFilter);
		
		otherFilters.add(getCompomemtsFilter());
		
		filters.setOther(otherFilters);
		return filters;
	}
	
	@PostMapping("/api/v1/filters/spells/{englishClassName}")
	public FilterApi getSpellsByClassFilter(@PathVariable String englishClassName) {
		FilterApi filters = new FilterApi();

		HeroClass heroClass = classRepository.findByEnglishName(englishClassName.replace('_', ' '));
		List<FilterApi> otherFilters = new ArrayList<>();
		otherFilters.add(getLevelsFilter(heroClass.getSpellcasterType().getMaxSpellLevel()));
		otherFilters.add(getCompomemtsFilter());
		filters.setOther(otherFilters);
		
		List<FilterApi> customFilters = new ArrayList<>();
		FilterApi customFilter = new FilterApi();
		customFilter.setName("Классы");
		customFilter.setKey("class");

		FilterValueApi customValue = new FilterValueApi();
		customValue.setLabel(heroClass.getCapitalazeName());
		customValue.setDefaultValue(Boolean.TRUE);
		customValue.setKey(String.valueOf(heroClass.getId()));
		customFilter.setValues(Collections.singletonList(customValue));
		customFilters.add(customFilter);
		filters.setCustom(customFilters);
		return filters;
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
		FilterApi schoolSpellFilter = new FilterApi("Характеристики", "ability");
		schoolSpellFilter.setValues(
				AbilityType.getBaseAbility().stream()
				 .map(ability -> new FilterValueApi(ability.getCyrilicName(), ability.name(), Boolean.TRUE))
				 .collect(Collectors.toList()));
		
		otherFilters.add(schoolSpellFilter);
		filters.setOther(otherFilters);
		return filters;
	}
	
	@PostMapping("/api/v1/filters/options")
	public FilterApi getOptionFilter() {
		FilterApi filters = new FilterApi();
		List<FilterApi> sources = new ArrayList<>();
		FilterApi spellMainFilter = new FilterApi("main");
		spellMainFilter.setValues(
				optionRepository.findBook(TypeBook.OFFICAL).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(spellMainFilter);
		
		FilterApi settingFilter = new FilterApi("Сеттинги", "settings");
		settingFilter.setValues(
				optionRepository.findBook(TypeBook.SETTING).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(settingFilter);
		
		FilterApi adventureFilter = new FilterApi("Приключения", "adventures");
		adventureFilter.setValues(
				optionRepository.findBook(TypeBook.MODULE).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(adventureFilter);
		
		FilterApi homebrewFilter = new FilterApi("Homebrew", "homebrew");
		homebrewFilter.setValues(
				optionRepository.findBook(TypeBook.CUSTOM).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(homebrewFilter);
		filters.setSources(sources);
		
		List<FilterApi> otherFilters = new ArrayList<>();
		
		FilterApi classOptionFilter = new FilterApi("Классовые особености", "classOption");
		classOptionFilter.setValues(
				Arrays.stream(Option.OptionType.values())
				 .map(ability -> new FilterValueApi(ability.getName(), ability.name(), Boolean.TRUE))
				 .collect(Collectors.toList()));
		otherFilters.add(classOptionFilter);
		
		FilterApi prerequisiteFilter = new FilterApi("Требования", "prerequsite");
		prerequisiteFilter.setValues(
				optionRepository.findAlldPrerequisite().stream()
				 .map(ability -> new FilterValueApi(ability, ability, Boolean.TRUE))
				 .collect(Collectors.toList()));
		otherFilters.add(prerequisiteFilter);
		
		filters.setOther(otherFilters);
		return filters;
	}
	
	@PostMapping("/api/v1/filters/backgrounds")
	public FilterApi getBackgroundFilter() {
		FilterApi filters = new FilterApi();
		List<FilterApi> sources = new ArrayList<>();
		FilterApi spellMainFilter = new FilterApi("main");
		spellMainFilter.setValues(
				backgroundRepository.findBook(TypeBook.OFFICAL).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(spellMainFilter);
		
		FilterApi settingFilter = new FilterApi("Сеттинги", "settings");
		settingFilter.setValues(
				backgroundRepository.findBook(TypeBook.SETTING).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(settingFilter);
		
		FilterApi adventureFilter = new FilterApi("Приключения", "adventures");
		adventureFilter.setValues(
				backgroundRepository.findBook(TypeBook.MODULE).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(adventureFilter);
		
		FilterApi homebrewFilter = new FilterApi("Homebrew", "homebrew");
		homebrewFilter.setValues(
				backgroundRepository.findBook(TypeBook.CUSTOM).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(homebrewFilter);
		filters.setSources(sources);
		
		List<FilterApi> otherFilters = new ArrayList<>();
		FilterApi schoolSpellFilter = new FilterApi("Навыки", "skills");
		schoolSpellFilter.setValues(
				Arrays.stream(SkillType.values())
				 .sorted(Comparator.comparing(SkillType::getCyrilicName))
				 .map(ability -> new FilterValueApi(ability.getCyrilicName(), ability.name(), Boolean.TRUE))
				 .collect(Collectors.toList()));
		
		otherFilters.add(schoolSpellFilter);
		filters.setOther(otherFilters);
		return filters;
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
				 .map(value -> new FilterValueApi(value.getCyrilicName(), value.name(), Boolean.TRUE))
				 .collect(Collectors.toList()));
		otherFilters.add(damageTypeFilter);

		FilterApi properetyTypeFilter = new FilterApi("По свойствам", "properrty");
		properetyTypeFilter.setValues(
				propertyRepository.findAll().stream()
				 .map(value -> new FilterValueApi(value.getName(), value.getId(), Boolean.TRUE))
				 .collect(Collectors.toList()));
		otherFilters.add(properetyTypeFilter);

		FilterApi diceFilter = new FilterApi("По кости урона", "dice");
		diceFilter.setValues(
				Arrays.stream(new String[]{"к4", "2к4", "к6", "2к6", "к8", "к10", "к12"})
				 .map(value -> new FilterValueApi(value, value, Boolean.TRUE))
				 .collect(Collectors.toList()));
		otherFilters.add(diceFilter);
		
		filters.setOther(otherFilters);
		return filters;
	}
	
	@PostMapping("/api/v1/filters/items")
	public FilterApi getItemsFilter() {
		FilterApi filters = new FilterApi();
		List<FilterApi> sources = new ArrayList<>();
		FilterApi spellMainFilter = new FilterApi("main");
		spellMainFilter.setValues(
				itemRepository.findBook(TypeBook.OFFICAL).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(spellMainFilter);
		
		FilterApi settingFilter = new FilterApi("Сеттинги", "settings");
		settingFilter.setValues(
				itemRepository.findBook(TypeBook.SETTING).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(settingFilter);
		
		FilterApi adventureFilter = new FilterApi("Приключения", "adventures");
		adventureFilter.setValues(
				itemRepository.findBook(TypeBook.MODULE).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(adventureFilter);
		
		FilterApi homebrewFilter = new FilterApi("Homebrew", "homebrew");
		homebrewFilter.setValues(
				itemRepository.findBook(TypeBook.CUSTOM).stream()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		sources.add(homebrewFilter);
		filters.setSources(sources);
		
		List<FilterApi> otherFilters = new ArrayList<>();
		
		FilterApi damageTypeFilter = new FilterApi("Категория", "category");
		damageTypeFilter.setValues(
				Arrays.stream(EquipmentType.values())
				 .sorted(Comparator.comparing(EquipmentType::getCyrilicName))
				 .map(value -> new FilterValueApi(value.getCyrilicName(), value.name(), Boolean.TRUE))
				 .collect(Collectors.toList()));
		otherFilters.add(damageTypeFilter);

		filters.setOther(otherFilters);
		return filters;
	}
	
	private FilterApi getLevelsFilter(int maxLevel) {
		FilterApi levelFilter = new FilterApi("Уровень", "level");
		levelFilter.setValues(IntStream.rangeClosed(0, maxLevel)
				 .mapToObj(level -> new FilterValueApi(level == 0 ? "заговор" : String.valueOf(level),  String.valueOf(level), Boolean.TRUE))
				 .collect(Collectors.toList()));
		return levelFilter;
	}
	
	private FilterApi getCompomemtsFilter() {
		FilterApi componentsSpellFilter = new FilterApi("Компоненты", "components");
		List<FilterValueApi> componentValues = new ArrayList<>();
		componentValues.add(new FilterValueApi("вербальный", "1", Boolean.TRUE));
		componentValues.add(new FilterValueApi("соматический", "2", Boolean.TRUE));
		componentValues.add(new FilterValueApi("материальный", "3", Boolean.TRUE));
		componentValues.add(new FilterValueApi("расходуемый", "4", Boolean.TRUE));
		componentValues.add(new FilterValueApi("не расходуемый", "5", Boolean.TRUE));
		
		componentsSpellFilter.setValues(componentValues);
		return componentsSpellFilter;
	}
	
	private FilterApi getClassFilters() {
		FilterApi filters = new FilterApi();
		List<FilterApi> classSources = new ArrayList<>();

		FilterApi mainFilter = new FilterApi("main");
		mainFilter.setValues(
				Stream.concat(classRepository.findBook(TypeBook.OFFICAL).stream(), archetypeRepository.findBook(TypeBook.OFFICAL).stream())
				.distinct()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		classSources.add(mainFilter);
		
		FilterApi settingFilter = new FilterApi("Сеттинги", "settings");
		settingFilter.setValues(
				Stream.concat(classRepository.findBook(TypeBook.SETTING).stream(), archetypeRepository.findBook(TypeBook.SETTING).stream())
				.distinct()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		classSources.add(settingFilter);

		FilterApi homebrewFilter = new FilterApi("Homebrew", "homebrew");
		homebrewFilter.setValues(
				Stream.concat(classRepository.findBook(TypeBook.CUSTOM).stream(), archetypeRepository.findBook(TypeBook.CUSTOM).stream())
				.distinct()
				.map(book -> new FilterValueApi(book.getSource(), book.getSource(),	Boolean.TRUE, book.getName()))
				.collect(Collectors.toList()));
		classSources.add(homebrewFilter);
		
		filters.setSources(classSources);
		
		List<FilterApi> others = new ArrayList<>();
		FilterApi hillDiceFilter = new FilterApi("Кость хитов", "hitdice");
		hillDiceFilter.setValues(
				EnumSet.of(Dice.d6, Dice.d8, Dice.d10, Dice.d12).stream()
				.map(dice -> new FilterValueApi(dice.getName(), dice.name(), Boolean.TRUE))
				.collect(Collectors.toList())
		);
		others.add(hillDiceFilter);
		filters.setOther(others);
		return filters;
	}
}