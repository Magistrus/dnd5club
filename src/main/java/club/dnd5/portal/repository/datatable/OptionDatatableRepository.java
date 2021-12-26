package club.dnd5.portal.repository.datatable;

import java.util.Collection;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.classes.Option;

@Repository
public interface OptionDatatableRepository extends DataTablesRepository<Option, Integer> {
	Option findByEnglishName(String name);

	@Query("SELECT o.prerequisite FROM Option o WHERE o.prerequisite IS NOT NULL GROUP BY o.prerequisite")
	Collection<String> finAlldPrerequisite();
}