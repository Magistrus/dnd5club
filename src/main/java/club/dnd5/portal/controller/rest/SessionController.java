package club.dnd5.portal.controller.rest;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {
	@Autowired
	private HttpSession session;
	
	@GetMapping("/session/theme")
	public String getTheme()
	{
		Object themeName = session.getAttribute("theme");
		return themeName == null ? "light" : themeName.toString();
	}
	
	@PostMapping("/session/theme")
	public ResponseEntity<String> setTheme(String theme)
	{
		session.setAttribute("theme", theme);
		return ResponseEntity.ok(theme);
	}
	
	@PostMapping("/session/menu")
	public ResponseEntity<String> setMenuState(String state)
	{
		session.setAttribute("compact_menu", state);
		return ResponseEntity.ok(state);
	}
}