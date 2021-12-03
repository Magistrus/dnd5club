package club.dnd5.portal.repository.datatable;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.items.Treasure;
import club.dnd5.portal.model.items.TreasureType;

@Repository
public interface TreasureDatatableRepository extends DataTablesRepository<Treasure, Integer> {
	Treasure findByEnglishName(String name);
	List<Treasure> findAllByCostAndType(int cost, TreasureType type);
}