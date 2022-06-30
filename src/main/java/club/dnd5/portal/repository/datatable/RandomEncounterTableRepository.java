package club.dnd5.portal.repository.datatable;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.encounters.RandomEncounterеTable;

public interface RandomEncounterTableRepository extends JpaRepository<RandomEncounterеTable, Integer> {
	Optional<RandomEncounterеTable> findByLevelAndType(int level, HabitatType type);
	
}