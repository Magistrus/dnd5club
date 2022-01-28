package club.dnd5.portal.controller;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import club.dnd5.portal.dto.bestiary.BeastForm;
import club.dnd5.portal.model.Alignment;
import club.dnd5.portal.model.ArmorType;
import club.dnd5.portal.model.CreatureSize;
import club.dnd5.portal.model.CreatureType;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.Dice;
import club.dnd5.portal.model.creature.Condition;
import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.repository.datatable.BestiaryDatatableRepository;

@Controller
public class ModeratorController {
	@Autowired
	private BestiaryDatatableRepository repo;
	
	@GetMapping ("/profile/beast")
	public String getProfileForm(Model model) {
		model.addAttribute("sizes", CreatureSize.getFilterSizes());
		model.addAttribute("types", CreatureType.values());
		model.addAttribute("aligments", Alignment.values());
		model.addAttribute("resistancs", DamageType.getResistance());
		model.addAttribute("immunities", DamageType.getImmunity());
		model.addAttribute("vulnerabilities", DamageType.getVulnerability());
		model.addAttribute("conditionImmunity", Condition.values());
		model.addAttribute("hitDices", Dice.getCreatures());
		model.addAttribute("armorTypes", ArmorType.getCreatures());
		model.addAttribute("habitates", HabitatType.values());
		return "user/admin/add_beast";
	}

	@GetMapping ("/profile/beast/trait")
	public String getTraitForm(Model model) {
		return "user/admin/feature :: trait";
	}

	@GetMapping ("/profile/beast/action")
	public String getActionForm(Model model) {
		return "user/admin/feature :: action";
	}
	
	@GetMapping ("/profile/beast/reaction")
	public String getReactionForm(Model model) {
		return "user/admin/feature :: reaction";
	}

	@GetMapping ("/profile/beast/bonus")
	public String getBonusActionForm(Model model) {
		return "user/admin/feature :: bonus";
	}
	
	@GetMapping ("/profile/beast/legendary")
	public String getLegendaryActionForm(Model model) {
		return "user/admin/feature :: legendary";
	}
	
	@GetMapping("/admin/bestiary/{id}")
	public String getBeastForm(Model model, @PathVariable Integer id) {
		model.addAttribute("sizes", CreatureSize.getFilterSizes());
		model.addAttribute("types", CreatureType.values());
		model.addAttribute("aligments", Alignment.values());
		model.addAttribute("resistancs", DamageType.getResistance());
		model.addAttribute("immunities", DamageType.getImmunity());
		model.addAttribute("vulnerabilities", DamageType.getVulnerability());
		model.addAttribute("conditionImmunity", Condition.values());
		model.addAttribute("hitDices", Dice.getCreatures());
		model.addAttribute("armorTypes", ArmorType.getCreatures());
		model.addAttribute("habitates", HabitatType.values());
	
		Creature beast = repo.findById(id).get();
		model.addAttribute("beastForm", new BeastForm(beast)); 
		return "user/admin/edit_beast";
	}
	
	@Transactional
	@PostMapping("/admin/bestiary/{id}")
	public String updateBeast(Model model, @PathVariable Integer id, BeastForm beastForm, @RequestParam Map<String, String> params) {
		//repo.save(beastForm.build());
		beastForm.build();
		//model.addAttribute("selectedCreature", beast);
		return "redirect:/bestiary/" + beastForm.getEnglishName().replace(' ', '_');
	}
}