package club.dnd5.portal.repository.datatable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.items.Treasure;

@Repository
public interface TreasureDatatableRepository extends DataTablesRepository<Treasure, Integer> {
	Treasure findByEnglishName(String name);
}