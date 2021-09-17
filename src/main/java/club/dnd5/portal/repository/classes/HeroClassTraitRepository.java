package club.dnd5.portal.repository.classes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.classes.HeroClassTrait;

@Repository
public interface HeroClassTraitRepository extends JpaRepository<HeroClassTrait, Integer>{

}
