package club.dnd5.portal.repository.tavern;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import club.dnd5.portal.model.tavern.TavernaCategory;
import club.dnd5.portal.model.tavern.TavernaType;
import club.dnd5.portal.model.tavern.Visitor;


public interface VisitorRepository extends JpaRepository<Visitor, Integer> {
	public final static String QUERY = 
			"SELECT v                                                     " + 
            "FROM Visitor v LEFT JOIN v.chance c                          " +
            "WHERE c.tavernaType = :type AND c.tavernaCategory = :category";

	@Query(QUERY)
	List<Visitor> findByTavernaTypeAndTavernaCategory(@Param("type") TavernaType type, @Param("category") TavernaCategory category);
} 