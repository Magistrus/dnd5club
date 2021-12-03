package club.dnd5.portal.repository.tavern;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.tavern.TavernaPrefixName;

@Repository
public interface TavernaPrefixNameRepository extends JpaRepository<TavernaPrefixName, Integer> {

}