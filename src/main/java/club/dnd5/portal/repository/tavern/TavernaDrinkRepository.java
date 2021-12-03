package club.dnd5.portal.repository.tavern;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.tavern.TavernaCategory;
import club.dnd5.portal.model.tavern.TavernaDrink;

public interface TavernaDrinkRepository extends JpaRepository<TavernaDrink, Integer>{
	List<TavernaDrink> findByCategory(TavernaCategory category);
	List<TavernaDrink> findByHabitat(HabitatType habitat);
}