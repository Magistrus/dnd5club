package club.dnd5.portal.controller.tools;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.races.Sex;
import club.dnd5.portal.model.tavern.TavernaName;
import club.dnd5.portal.model.tavern.TavernaPrefixName;
import club.dnd5.portal.model.tavern.TavernaType;
import club.dnd5.portal.repository.classes.ClassRepository;
import club.dnd5.portal.repository.classes.RaceRepository;
import club.dnd5.portal.repository.tavern.AtmosphereRepoditory;
import club.dnd5.portal.repository.tavern.DrinkEffectsRepository;
import club.dnd5.portal.repository.tavern.RandomEventRepository;
import club.dnd5.portal.repository.tavern.TavernaDishRepository;
import club.dnd5.portal.repository.tavern.TavernaDrinkRepository;
import club.dnd5.portal.repository.tavern.TavernaNameRepository;
import club.dnd5.portal.repository.tavern.TavernaPrefixNameRepository;
import club.dnd5.portal.repository.tavern.TopicDiscussedRepository;
import club.dnd5.portal.repository.tavern.VisitorRepository;

@Controller
public class TavernToolController {
	private static final Random rnd = new Random();
	private static final List<HabitatType> habitates = EnumSet.of(HabitatType.SWAMP, HabitatType.CITY,
			HabitatType.MOUNTAIN, HabitatType.VILLAGE, HabitatType.UNDERGROUND, HabitatType.ARCTIC, HabitatType.WATERS,
			HabitatType.DESERT, HabitatType.GRASSLAND, HabitatType.FOREST, HabitatType.TROPICS).stream()
			.collect(Collectors.toList());

	@Autowired
	private TavernaNameRepository nameRepo;
	@Autowired
	private TavernaPrefixNameRepository prefixRepo;
	@Autowired
	private RaceRepository raceRepo;
	@Autowired
	private AtmosphereRepoditory atmosphereRepo;
	@Autowired
	private TopicDiscussedRepository topicRepo;
	@Autowired
	private RandomEventRepository eventRepo;
	@Autowired
	private VisitorRepository visitorRepo;
	@Autowired
	private ClassRepository classRepo;
	@Autowired
	private TavernaDishRepository dishRepo;
	@Autowired
	private TavernaDrinkRepository drinkRepo;
	@Autowired
	private DrinkEffectsRepository drinkEffectRepo;
	
	private Set<String> generatedNames = new HashSet<>();

	@GetMapping("/tools/tavern")
	public String getForm() {
		return "tools/tavern";
	}

	@GetMapping("/tools/tavern/name")
	@ResponseBody
	public String getTreasuryTool(String tavernaType) {
		List<TavernaName> tavernaNames = nameRepo.findAll();
		List<TavernaPrefixName> prefixes = prefixRepo.findAll();
		TavernaType type;
		if (tavernaType == null) {
			type = TavernaType.values()[rnd.nextInt(TavernaType.values().length)];
		} else {
			type = TavernaType.valueOf(tavernaType);
		}
		String tavernName = null;
		do {
			int index = rnd.nextInt(tavernaNames.size());
			TavernaName tavernaName = tavernaNames.get(index);
			index = rnd.nextInt(prefixes.size());
			TavernaPrefixName prefix = prefixes.get(index);
			if (prefix.getObjectType() != null) {
				tavernaNames = tavernaNames.stream()
						.filter(n -> n.getObjectType() == prefix.getObjectType())
						.collect(Collectors.toList());
				index = rnd.nextInt(tavernaNames.size());
				tavernaName = tavernaNames.get(index);
			}
			int nameType = rnd.nextInt(100);
			if (nameType > 85) {
				tavernaNames = tavernaNames.stream().filter(n -> n.getNames() != null).collect(Collectors.toList());
				index = rnd.nextInt(tavernaNames.size());
				TavernaName name = tavernaNames.get(index);
				tavernName = type.getName() + " \"";
				switch (rnd.nextInt(5)) {
				case 0:
					tavernName += "Три ";
					break;
				case 1:
					tavernName += "Четыре ";
					break;
				default:
					tavernName += name.getSex() == Sex.FEMALE ? "Две " : "Два ";
					break;
				}
				tavernName += tavernaNames.get(index).getNames() + "\"";
			} else if (nameType > 70) {
				index = rnd.nextInt(tavernaNames.size());
				TavernaName tavernaName2 = tavernaNames.get(index);
				tavernName = type.getName() + " \"" + tavernaName.getName() + " и " + tavernaName2.getName() + "\"";
			} else if (nameType > 60) {
				index = rnd.nextInt(tavernaNames.size());
				TavernaName tavernaName2 = tavernaNames.get(index);
				tavernName = type.getName() + " \"" + prefix.getName(tavernaName.getSex()) + " " + tavernaName.getName()
						+ " и " + tavernaName2.getName() + "\"";
			} else {
				tavernName = type.getName() + " \"" + prefix.getName(tavernaName.getSex()) + " " + tavernaName.getName()
						+ "\"";
			}
		} while (generatedNames.contains(tavernName));
		if (generatedNames.size() < 500) {
			generatedNames.add(tavernName);
		} else {
			generatedNames.clear();
		}
		return tavernName;
	}
	
	@GetMapping("/tools/tavern/habitates/random")
	public String getHabitates(Model model) {
		model.addAttribute("selected",  habitates.get(rnd.nextInt(habitates.size())));
		model.addAttribute("habitates",  habitates);
		return "tools/tavern :: habitates";
	}
}