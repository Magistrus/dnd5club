package club.dnd5.portal.repository.tavern;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.tavern.RandomEvent;

@Repository
public interface RandomEventRepository  extends JpaRepository<RandomEvent, Integer>  {
	List<RandomEvent> findByVisitorsLessThanEqual(int visitors);
}
