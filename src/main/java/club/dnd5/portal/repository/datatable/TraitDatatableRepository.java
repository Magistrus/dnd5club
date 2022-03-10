package club.dnd5.portal.repository.datatable;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.trait.Trait;

@Repository
public interface TraitDatatableRepository extends DataTablesRepository<Trait, Integer> {
	Trait findByEnglishName(String name);
	
	@Query("SELECT t.requirement FROM Trait t GROUP BY t.requirement")
	List<String> findAllPrerequisite();
	
	@Query("SELECT c.book FROM Trait c GROUP BY c.book HAVING c.book.type = :type ORDER BY c.book.year")
	List<Book> findBook(@Param("type") TypeBook type);
}