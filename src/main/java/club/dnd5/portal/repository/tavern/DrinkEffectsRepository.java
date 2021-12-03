package club.dnd5.portal.repository.tavern;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.tavern.DrinkEffect;

@Repository
public interface DrinkEffectsRepository extends JpaRepository<DrinkEffect, Integer>{

}
