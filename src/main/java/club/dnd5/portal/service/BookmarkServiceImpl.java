package club.dnd5.portal.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import club.dnd5.portal.dto.api.bookmark.BookmarkApi;
import club.dnd5.portal.model.BookmarkCategory;
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
	public void addBookmark(User user, BookmarkApi bookmark) {
		Bookmark group;
		Bookmark category;
		Bookmark entityBookmark = new Bookmark();

		entityBookmark.setUser(user);
		entityBookmark.setUuid(getNewUUID());
		entityBookmark.setName(bookmark.getName());
		entityBookmark.setOrder(bookmark.getOrder());
		entityBookmark.setPrefix(bookmark.getPrefix());
		
		if (bookmark.getParentUUID() != null) {
			group = bookmarkRepository.findById(UUID.fromString(bookmark.getParentUUID()))
				.orElseThrow(() -> new RuntimeException("Bookmark's group not found"));

			entityBookmark.setParent(group);

			if (bookmark.getUrl() != null) {
				category = bookmarkRepository.findById(group.getUuid())
					.orElseGet(() -> getNewCategory(user, group, entityBookmark));

				entityBookmark.setParent(category);
				entityBookmark.setUrl(bookmark.getUrl());
			}
		}
		bookmarkRepository.save(entityBookmark);
	}

	@Override
	public void updateBookmarks(User user, List<BookmarkApi> bookmarks) {
		Collection<Bookmark> savedBookmarks = bookmarkRepository.findByUser(user);
		Set<String> savedIds = savedBookmarks
				.parallelStream()
				.map(Bookmark::getUuid)
				.map(UUID::toString)
				.collect(Collectors.toSet());
		
		Collection<Bookmark> updatedBookmarks = bookmarks
			.parallelStream()
			.filter(b -> savedIds.contains(b.getUuid()))
			.map(bookmark -> getUpdatedBookmark(user, bookmark))
			.collect(Collectors.toList());
		
		bookmarkRepository.saveAll(updatedBookmarks);
		
		bookmarks
				.stream()
				.filter(b -> !savedIds.contains(b.getUuid()))
				.forEach(bookmark -> saveNewBookmark(user, bookmark));

		Set<String> bookmarksIds = bookmarks
				.parallelStream()
				.map(BookmarkApi::getUuid)
				.collect(Collectors.toSet());
	
		Set<UUID> deleteBookmarksIds = bookmarks
				.parallelStream()
				.filter(b -> !bookmarksIds.contains(b.getUuid().toString()))
				.map(BookmarkApi::getUuid)
				.map(UUID::fromString)
				.collect(Collectors.toSet());
		bookmarkRepository.deleteAllById(deleteBookmarksIds);
	}

	@Override
	public void deleteBookmark(String uuid) {
		bookmarkRepository.deleteById(UUID.fromString(uuid));
	}

	@Override
	public void mergeBookmarks(User user, List<BookmarkApi> bookmarksApi) {
		Optional<Bookmark> rootBookmark = bookmarkRepository.findByUserAndOrder(user , -1);
		if (rootBookmark.isPresent()) {
			List<Bookmark> bookmarks = rootBookmark.get().getChildren()
					.parallelStream()
					.flatMap(b -> Stream.concat(Stream.of(b), b.getChildren().stream()))
					.collect(Collectors.toList());
			bookmarks.add(rootBookmark.get());
			for (BookmarkApi bookmarkApi : bookmarksApi) {
 				if (bookmarks.parallelStream().anyMatch(b -> b.getName().equals(bookmarkApi.getName()))) {
					continue;
				}
				Bookmark bookmark = new Bookmark();
				if (bookmarkRepository.existsById(UUID.fromString(bookmarkApi.getUuid()))) {
					bookmark.setUuid(getNewUUID());
				}
				else {
					bookmark.setUuid(UUID.fromString(bookmarkApi.getUuid()));
				}
				if (bookmarkApi.getParentUUID() != null) {
					Bookmark parent = bookmarkRepository.getById(UUID.fromString(bookmarkApi.getParentUUID()));
					bookmark.setParent(parent);
				}
				bookmark.setName(bookmarkApi.getName());
				bookmark.setOrder(bookmarkApi.getOrder());
				bookmark.setUrl(bookmarkApi.getUrl());
				bookmark.setPrefix(bookmarkApi.getPrefix());
				bookmark.setUser(user);
				bookmarkRepository.save(bookmark);
			}
		} else {
			for (BookmarkApi bookmarkApi : bookmarksApi) {
				Bookmark bookmark = new Bookmark();
				if (bookmarkRepository.existsById(UUID.fromString(bookmarkApi.getUuid()))) {
					bookmark.setUuid(getNewUUID());
				}
				else {
					bookmark.setUuid(UUID.fromString(bookmarkApi.getUuid()));
				}
				if (bookmarkApi.getParentUUID() != null) {
					Bookmark parent = bookmarkRepository.getById(UUID.fromString(bookmarkApi.getParentUUID()));
					bookmark.setParent(parent);
				}
				bookmark.setName(bookmarkApi.getName());
				bookmark.setOrder(bookmarkApi.getOrder());
				bookmark.setUrl(bookmarkApi.getUrl());
				bookmark.setPrefix(bookmarkApi.getPrefix());
				bookmark.setUser(user);
				bookmarkRepository.save(bookmark);
			}
		}
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

	private Bookmark getNewCategory(User user, Bookmark group, Bookmark bookmark) {
		Bookmark category = new Bookmark();
		category.setUuid(getNewUUID());
		category.setName(BookmarkCategory.getCategoryByURL(bookmark.getUrl()).getName());
		category.setOrder(BookmarkCategory.getCategoryByURL(bookmark.getUrl()).getOrder());
		category.setParent(group);
		category.setUser(user);

		return bookmarkRepository.saveAndFlush(category);
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
	
	private void saveNewBookmark(User user, BookmarkApi bookmark) {
		Bookmark newBookmark = new Bookmark();
		if (bookmarkRepository.existsById(UUID.fromString(bookmark.getUuid()))) {
			newBookmark.setUuid(getNewUUID());	
		}
		else {
			newBookmark.setUuid(UUID.fromString(bookmark.getUuid()));
		}
		newBookmark.setName(bookmark.getName());
		newBookmark.setOrder(bookmark.getOrder());
		newBookmark.setUser(user);
		newBookmark.setPrefix(bookmark.getPrefix());
		newBookmark.setUrl(bookmark.getUrl());
		if (bookmark.getParentUUID() != null) {
			Bookmark parent = bookmarkRepository.getById(UUID.fromString(bookmark.getParentUUID()));
			if (parent != null) {
				newBookmark.setParent(parent);	
			}
		}
		bookmarkRepository.saveAndFlush(newBookmark);
	}
}