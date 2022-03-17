package club.dnd5.portal.repository.datatable;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.background.Background;
import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;

@Repository
public interface BackgroundDatatableRepository extends DataTablesRepository<Background, Integer> {
	Background findByEnglishName(String name);

	@Query("SELECT c.book FROM Background c GROUP BY c.book HAVING c.book.type = :type ORDER BY c.book.year")
	List<Book> findBook(@Param("type") TypeBook type);
}