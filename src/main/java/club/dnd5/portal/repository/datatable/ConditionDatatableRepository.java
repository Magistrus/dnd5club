package club.dnd5.portal.repository.datatable;

import java.util.Optional;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.Condition;

@Repository
public interface ConditionDatatableRepository extends DataTablesRepository<Condition, Integer> {
	Optional<Condition> findByEnglishName(@Param("englishName") String englishName);
}