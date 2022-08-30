package club.dnd5.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class MainController {
	@GetMapping("/")
	public String getHome(Model model) {
		return "index";
	}
	
	@GetMapping("/search")
	public String getSearch(Model model) {
		return "search";
	}
	
	@GetMapping("/page")
	public String getPageText(Model model) {
		return "page";
	}
	
	@GetMapping("/telegram_bot")
	public String getPageTelegrammBot(Model model) {
		model.addAttribute("metaTitle", "DnD5Club Telegram Bot");
		model.addAttribute("metaUrl", "https://dnd5.club/telegram_bot");
		model.addAttribute("metaDescription", "Бот в Telegram, который поможет вам быстро найти нужные вам заклинания в компактном виде или бросить кубы, если вы забыли их дома.");
		return "telegram_bot";
	}

	@GetMapping("/fvtt_import")
	public String getPageFvttExport(Model model) {
		model.addAttribute("metaTitle", "DnD5Club импорт монстров в FVTT");
		model.addAttribute("metaUrl", "https://dnd5.club/fvtt_import");
		model.addAttribute("metaDescription", "Инструкция по импорту существ из Бестиария в FVTT.");
		return "fvtt_import";
	}
	
	@GetMapping("/bookmarks_info")
	public String getBookmarkInstruction(Model model) {
		model.addAttribute("metaTitle", "DnD5Club инструкция по закладкам");
		model.addAttribute("metaUrl", "https://dnd5.club/bookmarks_info");
		model.addAttribute("metaDescription", "Инструкция по использованию закладок.");
		return "bookmarks_info";
	}
	
}