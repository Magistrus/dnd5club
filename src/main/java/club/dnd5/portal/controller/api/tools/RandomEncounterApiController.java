package club.dnd5.portal.controller.api.tools;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.tools.RandomEncounterApi;
import club.dnd5.portal.dto.api.tools.RandomEncounterTableApi;
import club.dnd5.portal.dto.api.tools.RequestRandomEncounterApi;
import club.dnd5.portal.model.Dice;
import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.encounters.RandomEncounterRow;
import club.dnd5.portal.model.encounters.RandomEncounterеTable;
import club.dnd5.portal.repository.datatable.RandomEncounterRepository;
import club.dnd5.portal.repository.datatable.RandomEncounterTableRepository;


@RestController
public class RandomEncounterApiController {
	public static final Random rnd = new Random();
	
	@Autowired
	private RandomEncounterRepository repo;
	@Autowired
	private RandomEncounterTableRepository repoTable;
	
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
	public ResponseEntity<RandomEncounterTableApi> getTable(@RequestBody RequestRandomEncounterApi reques) {
		Optional<RandomEncounterеTable> table = repoTable.findByLevelAndType(reques.getLevel(), HabitatType.valueOf(reques.getEnviroment()));
		if (table.isPresent()) {
			RandomEncounterTableApi raTable = new RandomEncounterTableApi(table.get());
			return ResponseEntity.ok(raTable);
		}
		return ResponseEntity.notFound().build();
	}
}