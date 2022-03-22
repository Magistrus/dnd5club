package club.dnd5.portal.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.naming.directory.InvalidAttributesException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.dto.bestiary.CreatureDto;
import club.dnd5.portal.model.CreatureSize;
import club.dnd5.portal.model.CreatureType;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.datatable.BestiaryDatatableRepository;
import club.dnd5.portal.repository.datatable.TagBestiaryDatatableRepository;

@Controller
public class BestiaryController {
	@Autowired
	private BestiaryDatatableRepository repository;
	@Autowired
	private TagBestiaryDatatableRepository tagRepo;
	@Autowired
	private ImageRepository imageRepo;
	
    @Value("${git.commit.id}")
    private String version;
    
	private Map<TypeBook, List<Book>> sources;

	@PostConstruct
	public void init() {
		sources = new HashMap<>();
		sources.put(TypeBook.OFFICAL, repository.findBook(TypeBook.OFFICAL));
		sources.put(TypeBook.SETTING, repository.findBook(TypeBook.SETTING));
		sources.put(TypeBook.MODULE, repository.findBook(TypeBook.MODULE));
		sources.put(TypeBook.CUSTOM, repository.findBook(TypeBook.CUSTOM));
	}
	
	@GetMapping("/bestiary")
	public String getCreatures(Model model) {
		model.addAttribute("books", sources.get(TypeBook.OFFICAL));
		model.addAttribute("settingBooks", sources.get(TypeBook.SETTING));
		model.addAttribute("moduleBooks", sources.get(TypeBook.MODULE));
		model.addAttribute("hombrewBooks", sources.get(TypeBook.CUSTOM));
		model.addAttribute("types", CreatureType.getFilterTypes());
		model.addAttribute("sizes", CreatureSize.getFilterSizes());
		model.addAttribute("tags", tagRepo.findAll(Sort.by(Direction.ASC, "name")));
		model.addAttribute("habitates", HabitatType.values());
		model.addAttribute("metaTitle", "Бестиарий (Monster Manual) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/bestiary");
		model.addAttribute("metaDescription", "Бестиарий - существа для D&D 5 редакции");
		model.addAttribute("version", version);
		return "bestiary";
	}
	
	@GetMapping("/bestiary/{name}")
	public String getCreature(Model model, @PathVariable String name, HttpServletRequest request) {
		Creature beast = repository.findByEnglishName(name.replace("_", " "));
		if (beast == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		model.addAttribute("books", sources.get(TypeBook.OFFICAL));
		model.addAttribute("settingBooks", sources.get(TypeBook.SETTING));
		model.addAttribute("moduleBooks", sources.get(TypeBook.MODULE));
		model.addAttribute("hombrewBooks", sources.get(TypeBook.CUSTOM));
		CreatureDto creature = new CreatureDto(beast);
		model.addAttribute("selectedCreature", creature);
		model.addAttribute("types", CreatureType.getFilterTypes());
		model.addAttribute("sizes", CreatureSize.getFilterSizes());
		model.addAttribute("tags", tagRepo.findAll(Sort.by(Direction.ASC, "name")));
		model.addAttribute("habitates", HabitatType.values());
		model.addAttribute("metaTitle", String.format("%s (%s)", creature.getName(), creature.getEnglishName()) + " | Бестиарий D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/bestiary/" + name);
		model.addAttribute("metaDescription", String.format("%s (%s) - %s %s, %s с уровнем опасности %s", beast.getName(), beast.getEnglishName(), beast.getSizeName(), beast.getType().getCyrilicName(), beast.getAligment(), beast.getChallengeRating()));
		Collection<String> images = imageRepo.findAllByTypeAndRefId(ImageType.CREATURE, creature.getId());
		if (!images.isEmpty()) {
			model.addAttribute("metaImage", images.iterator().next());
		}
		return "bestiary";
	}
	
	@GetMapping("/bestiary/fragment/{id:\\d+}")
	public String getCreatureFragmentById(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		Creature creature = repository.findById(id).orElseThrow(InvalidAttributesException::new);
		model.addAttribute("creature", creature);
		model.addAttribute("firstElement", new FirstElement());
		Collection<String> images = imageRepo.findAllByTypeAndRefId(ImageType.CREATURE, creature.getId());
		model.addAttribute("images", images);
		return "fragments/creature :: view";
	}
	
	@GetMapping("/bestiary/description/{id:\\d+}")
	public String getCreatureDescription(Model model, @PathVariable Integer id) throws InvalidAttributesException {
		model.addAttribute("creature", repository.findById(id).orElseThrow(InvalidAttributesException::new));
		model.addAttribute("firstElement", new FirstElement());
		return "fragments/creature :: description";
	}

	public static class FirstElement{
		private boolean first = true;
		public boolean isFirst() {
			boolean first = this.first;
			this.first = false;
			return first;
		}
	}
}