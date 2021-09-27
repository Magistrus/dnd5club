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

import club.dnd5.portal.dto.item.ItemMagicDto;
import club.dnd5.portal.repository.datatable.MagicItemDatatableRepository;

@RestController
public class ItemMagicRestController {
	@Autowired
	private MagicItemDatatableRepository repo;

	@GetMapping("/data/items/magic")
	public DataTablesOutput<ItemMagicDto> getData(@Valid DataTablesInput input,
			@RequestParam Map<String, String> queryParameters) {
		return repo.findAll(input, ItemMagicDto::new);
	}
	
	@PostMapping("/items/magic")
	public ItemMagicDto getSpell(Integer id) {
		return new ItemMagicDto(repo.findById(id).orElseThrow(InvalidParameterException::new));
	}
}