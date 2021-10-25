package club.dnd5.portal.repository.datatable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.classes.Option;

@Repository
public interface OptionDatatableRepository extends DataTablesRepository<Option, Integer> {
	Option findByEnglishName(String name);
}