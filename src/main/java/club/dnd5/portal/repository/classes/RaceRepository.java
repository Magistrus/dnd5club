package club.dnd5.portal.repository.classes;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.races.Race;

@Repository
public abstract interface RaceRepository extends JpaRepository<Race, Integer> {
	Collection<Race> findAllByParent(Race race, Sort sort);
	
	Optional<Race> findByEnglishName(String name);
}