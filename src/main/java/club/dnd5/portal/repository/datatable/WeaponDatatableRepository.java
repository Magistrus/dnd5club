package club.dnd5.portal.repository.datatable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.items.Weapon;

@Repository
public interface WeaponDatatableRepository extends DataTablesRepository<Weapon, Integer> {
}