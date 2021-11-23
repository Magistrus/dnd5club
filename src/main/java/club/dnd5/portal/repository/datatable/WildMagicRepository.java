package club.dnd5.portal.repository.datatable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.splells.WildMagic;


@Repository
public interface WildMagicRepository extends JpaRepository<WildMagic, Integer> {

}