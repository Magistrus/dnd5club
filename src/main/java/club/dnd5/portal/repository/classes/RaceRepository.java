package club.dnd5.portal.repository.classes;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.book.Book;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.races.Race;

@Repository
public abstract interface RaceRepository extends JpaRepository<Race, Integer> {
	Collection<Race> findAllByParent(Race race, Sort sort);
	
	Optional<Race> findByEnglishName(String name);
	
	@Query("SELECT r FROM Race r WHERE r.parent.englishName = :raceName AND r.englishName = :subraceName")
	Optional<Race> findBySubrace(@Param("raceName") String raceName, @Param("subraceName") String subraceName);
	
	@Query("SELECT r.book FROM Race r GROUP BY r.book HAVING r.book.type = :type ORDER BY r.book.year")
	List<Book> findBook(@Param("type") TypeBook type);

}