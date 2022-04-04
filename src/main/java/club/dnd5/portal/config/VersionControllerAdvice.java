package club.dnd5.portal.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class VersionControllerAdvice {
	@Value("${git.commit.id}")
	private String version;

	@ModelAttribute
	public void handleRequest(HttpServletRequest request, Model model) {
		model.addAttribute("version", version);
	}
}