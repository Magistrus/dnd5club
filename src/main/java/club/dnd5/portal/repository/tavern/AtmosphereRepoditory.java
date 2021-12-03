package club.dnd5.portal.repository.tavern;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.tavern.Atmosphere;

@Repository
public interface AtmosphereRepoditory  extends JpaRepository<Atmosphere, Integer> {
	List<Atmosphere> findByVisitorsLessThanEqual(int visitors);
}
