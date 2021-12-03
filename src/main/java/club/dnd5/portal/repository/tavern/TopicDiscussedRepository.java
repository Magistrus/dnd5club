package club.dnd5.portal.repository.tavern;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.tavern.TopicDiscussed;

@Repository
public interface TopicDiscussedRepository  extends JpaRepository<TopicDiscussed, Integer>  {
	List<TopicDiscussed> findByVisitorsLessThanEqual(int visitors);
}
