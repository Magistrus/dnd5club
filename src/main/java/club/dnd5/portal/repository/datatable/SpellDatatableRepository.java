package club.dnd5.portal.repository.datatable;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.splells.Spell;

@Repository
public interface SpellDatatableRepository extends DataTablesRepository<Spell, Integer> {
	Spell findByEnglishName(String name);

	List<Spell> findByLevelAndBook_type(byte level, TypeBook type);

	@Query("SELECT s.book FROM Spell s GROUP BY s.book HAVING s.book.type = 'OFFICAL' ORDER BY s.book.year")
	List<Book> findBook();
	@Query("SELECT s.book FROM Spell s GROUP BY s.book HAVING s.book.type = 'CUSTOM' ORDER BY s.book.year")
	List<Book> findHomebrewBook();
	@Query("SELECT s.book FROM Spell s GROUP BY s.book HAVING s.book.type = 'SETTING' ORDER BY s.book.year")
	List<Book> findSettingBook();
	
	@Query("SELECT s.book FROM Spell s GROUP BY s.book HAVING s.book.type = :type ORDER BY s.book.year")
	List<Book> findBook(@Param("type") TypeBook type);
}