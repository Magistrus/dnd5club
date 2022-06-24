package club.dnd5.portal.repository.datatable;

import java.util.Collection;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.creature.CreatureRace;

@Repository
public interface TagBestiaryDatatableRepository extends DataTablesRepository<CreatureRace, Integer> {
	Collection<CreatureRace> findByOrderByName(); 
}