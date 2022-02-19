package club.dnd5.portal.repository.datatable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.creature.Creature;

@Repository
public interface BestiaryDatatableRepository extends DataTablesRepository<Creature, Integer> {
	Creature findByEnglishName(String name);
}