package club.dnd5.portal.repository.datatable;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.dto.name.IRaceName;
import club.dnd5.portal.model.races.RaceName;

@Repository
public interface NameRepository extends JpaRepository<RaceName, Integer> {
	@Query(value = "SELECT r.id AS raceId, r.name AS name FROM dnd5.race_names rn JOIN dnd5.races r ON rn.race_id = r.id WHERE r.parent_id IS NULL group by r.id, r.name ORDER BY r.name", nativeQuery = true)
	Collection<IRaceName> findAllRaces();
}