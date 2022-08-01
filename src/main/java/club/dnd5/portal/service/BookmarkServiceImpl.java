package club.dnd5.portal.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import club.dnd5.portal.dto.api.bookmark.BookmarkApi;
import club.dnd5.portal.model.user.Bookmark;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.user.BookmarkRepository;

@Service
public class BookmarkServiceImpl implements BookmarkService {
	@Autowired
	private BookmarkRepository bookmarkRepository;
	
	@Override
	public Collection<BookmarkApi> getBookmarks(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addBookmark(User user, BookmarkApi bookmark) {
		Bookmark entityBookmark = new Bookmark();
		entityBookmark.setUser(user);
		entityBookmark.setUuid(bookmark.getUuid());
		if (bookmark.getParentUuid() != null) {
			Optional<Bookmark> parent = bookmarkRepository.findById(bookmark.getParentUuid());
			
		}
	}

	@Override
	public void updateBookmark(User user, BookmarkApi bookmark) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBookmark(String uuid) {
		bookmarkRepository.deleteById(uuid);
	}

	@Override
	public boolean existUuid(String uuid) {
		return bookmarkRepository.existsById(uuid);
	}
}