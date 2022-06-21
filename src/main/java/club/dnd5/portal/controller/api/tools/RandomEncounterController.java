package club.dnd5.portal.controller.api.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.NameValueApi;
import club.dnd5.portal.dto.api.tools.RandomEncounterApi;
import club.dnd5.portal.dto.api.tools.RandomEncounterTableApi;
import club.dnd5.portal.dto.api.tools.RequestRandomEncounterApi;
import club.dnd5.portal.model.Dice;
import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.encounters.RandomEncounterRow;
import club.dnd5.portal.repository.datatable.RandomEncounterRepository;


@RestController
public class RandomEncounterController {
	public static final Random rnd = new Random();
	
	@Autowired
	private RandomEncounterRepository repo;
	
	@GetMapping("/api/v1/tools/encounters")
	public RandomEncounterApi getItems() {
		return new RandomEncounterApi(HabitatType.values());
	}
	
	@PostMapping("/api/v1/tools/encounters")
	public RandomEncounterApi getItems(@RequestBody RequestRandomEncounterApi reques) {
		RandomEncounterRow encounter = repo.findOne(Dice.d100.roll(), reques.getLevel(), HabitatType.valueOf(reques.getEnviroment()));
		return new RandomEncounterApi(encounter);
	}
	
	@PostMapping("/api/v1/tools/encounters/table")
	public RandomEncounterTableApi getTable(@RequestBody RequestRandomEncounterApi reques) {
		return null;
	}
}