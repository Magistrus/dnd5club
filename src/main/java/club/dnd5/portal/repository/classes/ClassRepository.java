package club.dnd5.portal.repository.classes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.classes.HeroClass;

@Repository
public abstract interface ClassRepository extends JpaRepository<HeroClass, Integer> {
	@Query("select h from HeroClass h inner join h.spells s where s.name = :spellName")
	List<HeroClass> findBySpellName(@Param("spellName") String paramString);

	HeroClass findByEnglishName(String name);

	@Query("SELECT c FROM HeroClass c WHERE c.book.type IN :types")
	List<HeroClass> findAllBySources(@Param("types") Iterable<TypeBook> types);
}