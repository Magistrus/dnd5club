package club.dnd5.portal.repository.tavern;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.tavern.TavernaCategory;
import club.dnd5.portal.model.tavern.TavernaDish;

public interface TavernaDishRepository extends JpaRepository<TavernaDish, Integer>{
	List<TavernaDish> findByCategory(TavernaCategory category);
	List<TavernaDish> findByHabitat(HabitatType habitat);
}
