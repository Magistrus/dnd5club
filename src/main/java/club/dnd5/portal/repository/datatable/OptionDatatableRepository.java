package club.dnd5.portal.repository.datatable;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.classes.Option;
import club.dnd5.portal.model.classes.Option.OptionType;

@Repository
public interface OptionDatatableRepository extends DataTablesRepository<Option, Integer> {
	Option findByEnglishName(String name);

	@Query("SELECT o.prerequisite FROM Option o WHERE o.prerequisite IS NOT NULL GROUP BY o.prerequisite")
	Collection<String> findAlldPrerequisite();

	@Query("SELECT o.prerequisite FROM Option o JOIN o.optionTypes t WHERE o.prerequisite IS NOT NULL AND t =:type GROUP BY o.prerequisite")
	Collection<String> findAlldPrerequisite(@Param("type") OptionType type);
	
	@Query("SELECT c.book FROM Option c GROUP BY c.book HAVING c.book.type = :type ORDER BY c.book.year")
	List<Book> findBook(@Param("type") TypeBook type);
}