package club.dnd5.portal.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.Condition;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Integer>{
	Optional<Condition> findByEnglishName(@Param("englishName") String englishName);
	Condition findOneByEnglishName(String name);
	Collection<Condition> findAllByType(Condition.Type type);
}