package club.dnd5.portal.repository.datatable;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.splells.WildMagic;


@Repository
public interface WildMagicRepository extends JpaRepository<WildMagic, Integer> {
	@Query("SELECT wm.book FROM WildMagic wm GROUP BY wm.book")
	Collection<Book> finAllBook();

	@Query("SELECT wm FROM WildMagic wm WHERE wm.book.source IN :sources")
	List<WildMagic> findAllByBook(@Param("sources") Collection<String> sources);
}