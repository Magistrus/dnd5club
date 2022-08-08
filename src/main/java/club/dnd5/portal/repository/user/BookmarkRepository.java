package club.dnd5.portal.repository.user;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.user.Bookmark;
import club.dnd5.portal.model.user.User;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {
	Collection<Bookmark> findByUser(User user);

	Collection<Bookmark> findByUserAndParentIsNull(User user);
	Collection<Bookmark> findByParent(Bookmark parent);
}
