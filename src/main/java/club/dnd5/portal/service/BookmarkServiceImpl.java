package club.dnd5.portal.service;

import club.dnd5.portal.dto.api.bookmark.BookmarkApi;
import club.dnd5.portal.model.BookmarkSection;
import club.dnd5.portal.model.user.Bookmark;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.user.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		category.setName(BookmarkSection.getSectionByURL(bookmark.getUrl()).getName());
		category.setOrder(BookmarkSection.getSectionByURL(bookmark.getUrl()).getOrder());
		category.setParent(group);
		category.setUser(user);

		return bookmarkRepository.saveAndFlush(category);
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

		if (bookmark.getParentUuid() != null) {
			group = bookmarkRepository.findById(UUID.fromString(bookmark.getParentUuid()))
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

	private Bookmark getUpdatedBookmark(User user, BookmarkApi bookmark) {
		Bookmark updatedBookmark = new Bookmark();

		updatedBookmark.setUuid(UUID.fromString(bookmark.getUuid()));
		updatedBookmark.setName(bookmark.getName());
		updatedBookmark.setOrder(bookmark.getOrder());
		updatedBookmark.setUser(user);

		if (bookmark.getParentUuid() != null) {
			Bookmark parent = bookmarkRepository.getById(UUID.fromString(bookmark.getParentUuid()));

			updatedBookmark.setParent(parent);
		}

		if (bookmark.getUrl() != null) {
			updatedBookmark.setUrl(bookmark.getUrl());
		}

		return updatedBookmark;
	}

	@Override
	public void updateBookmarks(User user, List<BookmarkApi> bookmarks) {
		Collection<Bookmark> savedBookmarks = bookmarkRepository.findByUserAndParentIsNull(user);
		Collection<Bookmark> updatedBookmarks = bookmarks
			.stream()
			.map(bookmark -> getUpdatedBookmark(user, bookmark))
			.collect(Collectors.toList());

		for (Bookmark bookmark : savedBookmarks) {
			boolean bookmarkInUpdated = updatedBookmarks
				.stream()
				.anyMatch(item -> item.getUuid().equals(bookmark.getUuid()));

			if (!bookmarkInUpdated) {
				deleteBookmark(bookmark.getUuid().toString());
			}
		}

		bookmarkRepository.saveAll(updatedBookmarks);
	}

	private List<Bookmark> getChildrenBookmarks(Bookmark parent) {
		List<Bookmark> bookmarks = new ArrayList<>();

		if (!parent.getChildren().isEmpty()) {
			bookmarks.addAll(parent.getChildren());
			bookmarks.addAll(
				bookmarks
					.stream()
					.filter(item -> !item.getChildren().isEmpty())
					.flatMap(item -> item.getChildren().stream())
					.collect(Collectors.toList())
			);
		}

		return bookmarks;
	}

	@Override
	public void deleteBookmark(String uuid) {
		Bookmark bookmark = bookmarkRepository.findById(UUID.fromString(uuid))
			.orElseThrow(() -> new RuntimeException("Bookmark not found"));

		Collection<Bookmark> deleteList = Stream.of(bookmark).collect(Collectors.toList());

		deleteList.addAll(getChildrenBookmarks(bookmark));

		bookmarkRepository.deleteAllById(deleteList.stream().map(Bookmark::getUuid).collect(Collectors.toList()));
	}
}