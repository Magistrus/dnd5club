package club.dnd5.portal.controller.api.bookmark;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.bookmark.BookmarkApi;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.user.UserRepository;
import club.dnd5.portal.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Bookmark", description = "The Bookmark API")
@RestController
@RequestMapping("/api/v1/bookmarks")
public class BookmarkApiContoller {
	@Autowired
	private BookmarkService service;
	
	@Autowired
	private UserRepository userRepository;
	
	@Operation(summary = "Gets all user bookmarks")
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public Collection<BookmarkApi> getBookmarks() {
		SecurityContext context = SecurityContextHolder.getContext();
		String userName = context.getAuthentication().getName(); 
		User user = userRepository.findByEmailOrUsername(userName, userName).orElseThrow(() -> new UsernameNotFoundException(userName));
		return service.getBookmarks(user);
	}
	
	@Operation(summary = "Checks if the uuid exists")
	@GetMapping("/{uuid}")
	public ResponseEntity<?> existUiidBookmark(@PathVariable String uuid) {
		if (service.existUuid(uuid)) {
			 return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Add new bookmark")
	@PostMapping
	public ResponseEntity<?> createBookamrk(@RequestBody BookmarkApi bookmarkApi){
		SecurityContext context = SecurityContextHolder.getContext();
		String userName = context.getAuthentication().getName(); 
		User user = userRepository.findByEmailOrUsername(userName, userName).orElseThrow(() -> new UsernameNotFoundException(userName));
		service.addBookmark(user, bookmarkApi);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Update bookmark")
	@PutMapping
	public ResponseEntity<?> updateBookamrk(@RequestBody BookmarkApi bookmarkApi){
		SecurityContext context = SecurityContextHolder.getContext();
		String userName = context.getAuthentication().getName(); 
		User user = userRepository.findByEmailOrUsername(userName, userName).orElseThrow(() -> new UsernameNotFoundException(userName));
		service.updateBookmark(user, bookmarkApi);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Delete bookmark")
	@DeleteMapping("/{uuid}")
	public ResponseEntity<?> deleteBookamrk(@PathVariable String uuid){
		service.deleteBookmark(uuid);
		return ResponseEntity.ok().build();
	}
}