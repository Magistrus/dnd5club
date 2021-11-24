package club.dnd5.portal.controller.tools;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import club.dnd5.portal.model.Madness;
import club.dnd5.portal.model.MadnessType;
import club.dnd5.portal.repository.MadnessRepository;

@Controller
public class MadnessToolController {
	public static final Random rnd = new Random();
	@Autowired
	private MadnessRepository madnessRepo;
	
	@GetMapping("/tools/madness")
	public String getTreasuryTool() {
		return "tools/madness";
	}

	@GetMapping("/tools/madness/random")
	@ResponseBody
	public String getMadnessRandomText(Model model, String type) {
		List<Madness> madnesses;
		if (type != null) {
			MadnessType madnessType = MadnessType.valueOf(type);
			madnesses = madnessRepo.findByMadnessType(madnessType);
		} else {
			madnesses = madnessRepo.findAll();
		}
		Madness madness = madnesses.get(rnd.nextInt(madnesses.size()));
		String duration;
		switch (madness.getMadnessType()) {
		case SHORT:
			duration = (1 + rnd.nextInt(10)) + " минут";
			break;
		case LONG:
			duration = (1 + rnd.nextInt(10)) * 10 + " часов";
			break;
		default:
			duration = "до излечения";
			break;
		}
		return String.format("<strong>Тип:</strong> %s <strong>Длительность:</strong> %s <br><br> %s",
				madness.getMadnessType().getCyrilicName(), duration, madness.getDescription());
	}
}