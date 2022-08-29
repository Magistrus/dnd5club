package club.dnd5.portal.service;

import java.util.Collection;
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
		return bookmarkRepository.findByUser(user)
			.stream()
			.map(BookmarkApi::new)
			.collect(Collectors.toList());
	}

	@Override
	public BookmarkApi addBookmark(User user, BookmarkApi bookmark) {
		Bookmark entityBookmark = new Bookmark();

		entityBookmark.setUser(user);
		entityBookmark.setUuid(getNewUUID());
		entityBookmark.setName(bookmark.getName());

		if (bookmark.getOrder() != null) {
			entityBookmark.setOrder(bookmark.getOrder());
		} else if (bookmark.getParentUUID() != null) {
			entityBookmark.setOrder(
				bookmarkRepository
					.findByParentUuid(UUID.fromString(bookmark.getParentUUID()))
					.size()
			);
		} else {
			entityBookmark.setOrder(
				bookmarkRepository
					.findByUserAndParentIsNull(user)
					.size()
			);
		}

		if (bookmark.getPrefix() != null) {
			entityBookmark.setPrefix(bookmark.getPrefix());
		}

		if (bookmark.getParentUUID() != null) {
			Bookmark parent = bookmarkRepository.findById(UUID.fromString(bookmark.getParentUUID()))
				.orElseThrow(() -> new RuntimeException("Bookmark's group not found"));

			entityBookmark.setParent(parent);

			if (bookmark.getUrl() != null) {
				entityBookmark.setUrl(bookmark.getUrl());
			}
		}

		return new BookmarkApi(bookmarkRepository.saveAndFlush(entityBookmark));
	}

	@Override
	public BookmarkApi updateBookmark(User user, BookmarkApi bookmark) {
		return new BookmarkApi(bookmarkRepository.saveAndFlush(getUpdatedBookmark(user, bookmark)));
	}

	@Override
	public void deleteBookmark(String uuid) {
		bookmarkRepository.deleteById(UUID.fromString(uuid));
	}

	@Override
	public Collection<BookmarkApi> getRootBookmarks(User user) {
		return bookmarkRepository.findByUserAndParentIsNull(user)
				.stream()
				.map(BookmarkApi::new)
				.collect(Collectors.toList());
	}

	private UUID getNewUUID() {
		UUID uuid = UUID.randomUUID();
		if (bookmarkRepository.existsById(uuid)) {
			uuid = getNewUUID();
		}
		return uuid;
	}

	private Bookmark getUpdatedBookmark(User user, BookmarkApi bookmark) {
		Bookmark updatedBookmark = new Bookmark();

		updatedBookmark.setUuid(UUID.fromString(bookmark.getUuid()));
		updatedBookmark.setName(bookmark.getName());
		updatedBookmark.setOrder(bookmark.getOrder());
		updatedBookmark.setUser(user);
		updatedBookmark.setPrefix(bookmark.getPrefix());
		updatedBookmark.setUrl(bookmark.getUrl());
		if (bookmark.getParentUUID() != null) {
			Bookmark parent = bookmarkRepository.getById(UUID.fromString(bookmark.getParentUUID()));
			updatedBookmark.setParent(parent);
		}
		return updatedBookmark;
	}
}
