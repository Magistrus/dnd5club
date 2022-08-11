package club.dnd5.portal.service;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import club.dnd5.portal.dto.api.bookmark.BookmarkApi;
import club.dnd5.portal.model.user.User;

public interface BookmarkService {
	Collection<BookmarkApi> getBookmarks(User user);
	@Transactional
	void addBookmark(User user, BookmarkApi bookmark);
	@Transactional
	void updateBookmarks(User user, List<BookmarkApi> bookmarks);
	@Transactional
	void deleteBookmark(String uuid);
	void mergeBookmarks(User user, List<BookmarkApi> bookmarks);
	Collection<BookmarkApi> getParentBookmarks(User currentUser);
}
