package club.dnd5.portal.repository.datatable;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.screen.Screen;

@Repository
public interface ScreenDatatableRepository extends DataTablesRepository<Screen, Integer> {
	Collection<Screen> findAllByParentIsNull();
	Optional<Screen> findByEnglishName(@Param("englishName") String englishName);
}