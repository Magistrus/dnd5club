package club.dnd5.portal.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
		return bookmarkRepository.findByUserAndParentIsNull(user)
				.stream()
				.map(BookmarkApi::new)
				.collect(Collectors.toList());
	}

	@Override
	public void addBookmark(User user, BookmarkApi bookmark) {
		Bookmark entityBookmark = new Bookmark();
		entityBookmark.setUser(user);
		entityBookmark.setUuid(UUID.fromString(bookmark.getUuid()));
		entityBookmark.setName(bookmark.getName());
		entityBookmark.setUrl(bookmark.getUrl());
		entityBookmark.setOrder(bookmark.getOrder());
		if (bookmark.getParentUuid() != null) {
			Optional<Bookmark> parent = bookmarkRepository.findById(UUID.fromString(bookmark.getParentUuid()));
			if (parent.isPresent()) {
				entityBookmark.setParent(parent.get());
			}
		}
		bookmarkRepository.save(entityBookmark);
	}

	@Override
	public void updateBookmark(User user, BookmarkApi bookmark) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBookmark(String uuid) {
		bookmarkRepository.deleteById(UUID.fromString(uuid));
	}

	@Override
	public boolean existUuid(String uuid) {
		return bookmarkRepository.existsById(UUID.fromString(uuid));
	}
}