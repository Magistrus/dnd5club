package club.dnd5.portal.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class VersionControllerAdvice {
	@Value("${git.commit.id}")
	private String version;

	@Value("${spring.profiles.active}")
	private String profile;

	@ModelAttribute
	public void handleRequest(HttpServletRequest request, Model model) {
		model.addAttribute("version", version);
		model.addAttribute("profile", profile);
		model.addAttribute("themeName", "theme-dark");

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("theme_name")) {
					model.addAttribute(
						"themeName",
						new StringBuilder().append("theme-").append(cookie.getValue())
					);
				}
			}
		}
	}
}
