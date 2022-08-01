package club.dnd5.portal.service;

import java.util.Collection;

import javax.transaction.Transactional;

import club.dnd5.portal.dto.api.bookmark.BookmarkApi;
import club.dnd5.portal.model.user.User;

public interface BookmarkService {
	Collection<BookmarkApi> getBookmarks(User user);
	@Transactional
	void addBookmark(User user, BookmarkApi bookmark);
	@Transactional
	void updateBookmark(User user, BookmarkApi bookmark);
	@Transactional
	void deleteBookmark(String uuid);
	
	boolean existUuid(String uuid);
}