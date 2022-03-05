package club.dnd5.portal.repository.datatable;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.splells.Spell;

@Repository
public interface SpellDatatableRepository extends DataTablesRepository<Spell, Integer> {
	Spell findByEnglishName(String name);

	List<Spell> findByLevelAndBook_type(byte level, TypeBook type);

	@Query("SELECT s.book FROM Spell s GROUP BY s.book HAVING s.book.type != 'CUSTOM' ORDER BY (CASE WHEN s.book.type = 'OFFICAL' THEN 0 else 1 END)")
	List<Book> findBook();
	
	@Query("SELECT s.book FROM Spell s GROUP BY s.book HAVING s.book.type = 'CUSTOM'")
	List<Book> findHomebrewBook();
}