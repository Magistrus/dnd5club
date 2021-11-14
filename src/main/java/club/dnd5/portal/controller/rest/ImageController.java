package club.dnd5.portal.controller.rest;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.repository.ImageRepository;

@Controller
public class ImageController {
	@Autowired
	private ImageRepository repo;

	@Autowired
	private HttpSession session;

	@GetMapping("/image/{type}/{id}")
	@ResponseBody
	public String getImage(@PathVariable ImageType type, @PathVariable Integer id) {
		return repo.findAllByTypeAndRefId(type, id).stream().findFirst().orElse(getDefault(type));
	}

	private String getDefault(ImageType type) {
		if (type == ImageType.CREATURE) {
			Object themeObject = session.getAttribute("theme");
			String theme = null;
			if (themeObject == null) {
				theme = "light";
			} else {
				theme = themeObject.toString();
			}
			return "/resources/assets/style/" + theme + "/no_img_best.png";
		}
		return null;
	}
}