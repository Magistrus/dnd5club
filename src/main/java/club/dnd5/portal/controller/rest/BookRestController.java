package club.dnd5.portal.controller.rest;

import java.security.InvalidParameterException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.BookDto;
import club.dnd5.portal.repository.datatable.BookDatatableRepository;

@RestController
public class BookRestController {
	@Autowired
	private BookDatatableRepository repo;

	@GetMapping("/data/books")
	public DataTablesOutput<BookDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		return repo.findAll(input, BookDto::new);
	}
	
	@PostMapping("/books")
	public BookDto getSpell(String id) {
		return new BookDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
}