package club.dnd5.portal.repository.datatable;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.items.Treasure;
import club.dnd5.portal.model.items.TreasureType;

@Repository
public interface TreasureDatatableRepository extends DataTablesRepository<Treasure, Integer> {
	Treasure findByEnglishName(String name);
	List<Treasure> findAllByCostAndType(int cost, TreasureType type);
	List<Treasure> findAllByTypeIn(Set<TreasureType> types);
	
	@Query("SELECT c.book FROM Treasury c GROUP BY c.book HAVING c.book.type = :type ORDER BY c.book.year")
	List<Book> findBook(@Param("type") TypeBook type);
}