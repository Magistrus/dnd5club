package club.dnd5.portal.controller.api.bookmark;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.bookmark.BookmarkApi;
import club.dnd5.portal.service.BookmarkService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Bookmark", description = "The Bookmark API")
@RestController
@RequestMapping("/api/v1/bookmarks")
public class BookmarkApiContoller {
	@Autowired
	private BookmarkService service;
	
	@GetMapping
	public Collection<BookmarkApi> getBookmarks() {
		return null;
	}
	
	@PostMapping
	public ResponseEntity<?> createBookamrk(String uuid){
		service.deleteBookmark(uuid);
		return null;
	}

	@DeleteMapping
	public ResponseEntity<?> deleteBookamrk(){
		return null;
	}
}