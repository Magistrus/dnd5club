package club.dnd5.portal.repository.datatable;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.screen.Screen;

@Repository
public interface ScreenDatatableRepository extends DataTablesRepository<Screen, Integer> {
	Collection<Screen> findAllByParent(Screen race);
	Optional<Screen> findByEnglishName(String Screen);
}