package club.dnd5.portal.repository.datatable;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.items.WeaponProperty;

@Repository
public interface WeaponPropertyDatatableRepository extends DataTablesRepository<WeaponProperty, Integer> {
	List<WeaponProperty> findAll();

	WeaponProperty findByEnglishName(String englishName);
}