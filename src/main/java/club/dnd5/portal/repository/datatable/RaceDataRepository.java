package club.dnd5.portal.repository.datatable;

import java.util.Collection;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.races.Race;
import club.dnd5.portal.model.screen.Screen;

@Repository
public interface RaceDataRepository extends DataTablesRepository<Race, String> {
	Collection<Screen> findAllByParentIsNull();
}