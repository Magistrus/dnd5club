package club.dnd5.portal.repository.datatable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.trait.Trait;

@Repository
public interface TraitDatatableRepository extends DataTablesRepository<Trait, Integer> {
	Trait findByEnglishName(String name);
}