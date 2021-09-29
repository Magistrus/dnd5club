package club.dnd5.portal.repository.datatable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.items.Armor;

@Repository
public interface ArmorDatatableRepository extends DataTablesRepository<Armor, Integer> {
}