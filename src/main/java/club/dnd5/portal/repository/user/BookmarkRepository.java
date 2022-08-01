package club.dnd5.portal.repository.user;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import club.dnd5.portal.model.user.Bookmark;
import club.dnd5.portal.model.user.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, String>{
	Collection<Bookmark> findByUser(User user);
}