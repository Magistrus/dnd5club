package club.dnd5.portal.repository.datatable;

import java.util.List;

import org.springframework.data.jpa.datatables.mapping.SearchPanes.Item;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.god.God;

@Repository
public interface GodDatatableRepository extends DataTablesRepository<God, Integer> {
	@Query("SELECT g.pantheon.name AS label, g.pantheon.id AS value, 0 AS total, 0 AS count FROM God g GROUP BY g.pantheon")
	List<Item> countTotalGodPantheon();
	God findByEnglishName(String name);
}