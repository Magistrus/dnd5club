package club.dnd5.portal.repository.datatable;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.encounters.RandomEncounterRow;

public interface RandomEncounterRepository extends JpaRepository<RandomEncounterRow, Integer> {
	@Query("SELECT e FROM RandomEncounterRow e WHERE e.start >= :index AND e.end <= :index AND e.level = :level AND e.type = :type")
	Optional<RandomEncounterRow> findOne(
			@Param("index") int index,
			@Param("level") int level,
			@Param("type") HabitatType type);

	List<RandomEncounterRow> findAllByLevelAndType(Integer level, HabitatType type);
}