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

import club.dnd5.portal.dto.item.ItemDto;
import club.dnd5.portal.repository.datatable.ItemDatatableRepository;

@RestController
public class ItemRestController {
	@Autowired
	private ItemDatatableRepository repo;

	@GetMapping("/data/items")
	public DataTablesOutput<ItemDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		return repo.findAll(input, ItemDto::new);
	}
	
	@PostMapping("/items")
	public ItemDto getSpell(Integer id) {
		return new ItemDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
}