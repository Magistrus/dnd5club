package club.dnd5.portal.repository.datatable;

import java.util.Set;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.rule.Rule;

@Repository
public interface RuleDatatableRepository extends DataTablesRepository<Rule, Integer> {
	@Query("SELECT r.type FROM Rule r GROUP BY r.type")
	public Set<String> findAllCategories();
	Rule findByEnglishName(String name);
}