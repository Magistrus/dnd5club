package club.dnd5.portal.controller.api.bookmark;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.bookmark.BookmarkApi;
import club.dnd5.portal.dto.api.bookmark.CategoryApi;
import club.dnd5.portal.model.BookmarkCategory;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.user.UserRepository;
import club.dnd5.portal.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Bookmark", description = "The Bookmark API")
@RestController
@RequestMapping("/api/v1/bookmarks")
public class BookmarkApiController {
	@Autowired
	private BookmarkService service;
	@Autowired
	private UserRepository userRepository;

	@Operation(summary = "Gets all user bookmarks")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public Collection<BookmarkApi> getBookmarks(@RequestParam(required = false) Boolean parent) {
		if (parent != null) {
			return service.getParentBookmarks(getCurrentUser());
		}
		return service.getBookmarks(getCurrentUser());
	}

	@Operation(summary = "Add new bookmark")
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping
	public ResponseEntity<?> createBookmark(@RequestBody BookmarkApi bookmarkApi){
		service.addBookmark(getCurrentUser(), bookmarkApi);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Update bookmarks")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping
	public ResponseEntity<?> updateBookmarks(@RequestBody List<BookmarkApi> bookmarks){
		service.updateBookmarks(getCurrentUser(), bookmarks);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Merge bookmark")
	@SecurityRequirement(name = "Bearer Authentication")
	@PatchMapping
	public ResponseEntity<?> mergeBookmarks(@RequestBody List<BookmarkApi> bookmarks){
		service.mergeBookmarks(bookmarks);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Delete bookmark")
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping("/{uuid}")
	public ResponseEntity<?> deleteBookmark(@PathVariable String uuid){
		service.deleteBookmark(uuid);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Get all categories")
	@GetMapping("/categories")
	public ResponseEntity<List<BookmarkCategory>> getBookmarkCategories() {
		return ResponseEntity.ok(BookmarkCategory.getCategories());
	}

	@Operation(summary = "Get category from URL")
	@GetMapping("/category")
	public ResponseEntity<?> getBookmarkCategory(@RequestParam(required = false) String url,
			@RequestParam(required = false) String code) {
		if (url != null) {
			try {
				url = URLDecoder.decode(url, StandardCharsets.UTF_8.toString());
				return ResponseEntity.ok(new CategoryApi(BookmarkCategory.getCategoryByURL(url)));
			}
			catch (UnsupportedEncodingException exception) {
				return ResponseEntity.ok(new CategoryApi(BookmarkCategory.getDefaultCategory()));
			}
		}
		if (code != null) {
			return ResponseEntity.ok(new CategoryApi(BookmarkCategory.getCategoryByCode(code)));
		}
		return ResponseEntity.ok(new CategoryApi(BookmarkCategory.getDefaultCategory()));
	}
	
	private User getCurrentUser() {
		SecurityContext context = SecurityContextHolder.getContext();
		String userName = context.getAuthentication().getName();
		return userRepository.findByEmailOrUsername(userName, userName).orElseThrow(() -> new UsernameNotFoundException(userName));
	}
}