package club.dnd5.portal.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.image.Image;
import club.dnd5.portal.model.image.ImageType;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
	@Query("SELECT i.url FROM Image i WHERE i.type=:type AND i.refId=:refId")
	Collection<String> findAllByTypeAndRefId(@Param("type") ImageType type,@Param("refId") Integer refId);
}
