package club.dnd5.portal.repository.datatable;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.trait.Trait;

@Repository
public interface TraitDatatableRepository extends DataTablesRepository<Trait, Integer> {
	Trait findByEnglishName(String name);
	@Query("SELECT t.requirement FROM Trait t GROUP BY t.requirement")
	List<String> findAllPrerequisite();
}