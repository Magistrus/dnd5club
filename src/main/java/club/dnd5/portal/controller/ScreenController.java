package club.dnd5.portal.controller;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import club.dnd5.portal.model.screen.Screen;
import club.dnd5.portal.repository.datatable.ScreenDatatableRepository;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class ScreenController {
	@Autowired
	private ScreenDatatableRepository repository;

	@GetMapping("/screens")
	public String getScreens(Model model) {
		model.addAttribute("metaTitle", "Ширма Мастера (Screens) D&D 5e");
		model.addAttribute("metaUrl", "https://dnd5.club/screens");
		model.addAttribute("metaDescription", "Ширма Мастера Подземелий и Драконов по D&D 5 редакции");
		model.addAttribute("menuTitle", "Ширма Мастера");
		return "screens";
	}
	
	@GetMapping("/screens/{name}")
	public String getScreen(Model model, @PathVariable String name, HttpServletRequest request) {
		Screen screen = repository.findByEnglishName(name.replace('_', ' ')).get();
		if (screen == null) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, "404");
			return "forward: /error";
		}
		
		model.addAttribute("metaImage", screen.getIcon());
		model.addAttribute("metaTitle", String.format("%s (%s) - Ширма Мастера (Screens) D&D 5e", screen.getName(), screen.getEnglishName()));
		model.addAttribute("metaUrl", "https://dnd5.club/screens/" + name);
		model.addAttribute("metaDescription", String.format("%s (%s) Ширма Мастера Подземелий и Драконов по D&D 5 редакции", screen.getName(), screen.getEnglishName()));
		model.addAttribute("menuTitle", "Ширма Мастера");
		return "screens";
	}
	
	@GetMapping("/screens/{name}/{subscreen}")
	public String getSubscreenList(Model model, @PathVariable String name, @PathVariable String subscreen) {
		model.addAttribute("screens", repository.findAllByParentIsNullOrderByOrdering());
		model.addAttribute("selectedScreen", name);
		model.addAttribute("selectedSubscreen", subscreen);
		return "screens";
	}
	
	@GetMapping("/screens/fragment/{id:\\d+}")
	public String getScreenFragmentById(Model model, @PathVariable Integer id) {
		model.addAttribute("screens", repository.findById(id).get().getChields()
				.stream()
				.collect(Collectors.groupingBy(Screen::getCategory, LinkedHashMap::new, Collectors.toList())));
		return "fragments/screen :: view";
	}
	
	@GetMapping("/screens/fragmentone/{id:\\d+}")
	public String getScreenOneFragmentById(Model model, @PathVariable Integer id) {
		model.addAttribute("screen", repository.findById(id).get());
		return "fragments/screen :: viewOne";
	}
	
	@GetMapping("/screens/{englishName}/subscreens/list")
	public String getSubscreenList(Model model, @PathVariable String englishName) {
		Screen screen = repository.findByEnglishName(englishName.replace("_", " ")).orElseThrow(IllegalArgumentException::new);
		model.addAttribute("subscreens", screen.getChields().stream().filter(s -> s.getCategory() != null).collect(Collectors.groupingBy(Screen::getCategory)));
		return "fragments/subscreens_list :: sub_menu"; 
	}
	
	@GetMapping("/screens/{screenName}/subscreen/{subscreenName}")
	public String getFragmentSubraces(Model model, @PathVariable String screenName, @PathVariable String subscreenName) {
		Screen screen = repository.findByEnglishName(subscreenName.replace("_", " ")).orElseThrow(IllegalArgumentException::new);
		model.addAttribute("screen", screen);
		return "fragments/screen :: view";
	}
}