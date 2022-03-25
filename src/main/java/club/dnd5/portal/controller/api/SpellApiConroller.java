package club.dnd5.portal.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.spells.SpellApiDto;
import club.dnd5.portal.dto.api.spells.SpellRequestDto;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;

@RestController
@RequestMapping("/api")
public class SpellApiConroller {
	@Autowired
	private SpellDatatableRepository repo;
	
	@CrossOrigin
	@PostMapping(value = "/v1/spells", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SpellApiDto> getSpells(@RequestBody SpellRequestDto spellRequest){
		DataTablesInput input = new DataTablesInput();
		input.setDraw(2);
		List<Column> columns = new ArrayList<Column>();
		Column column = new Column();
		column.setData("name");
		column.setSearchable(true);
		column.setOrderable(true);
		columns.add(column);
		column = new Column();
		column.setData("englishName");
		column.setSearchable(true);
		columns.add(column);
		column = new Column();
		column.setData("level");
		column.setSearchable(false);
		columns.add(column);
		input.setColumns(columns);
		if (spellRequest.getLimit() != Integer.MAX_VALUE) {
			input.setLength(spellRequest.getLimit());
		}
		if (spellRequest.getPage()!= null) {
			input.setStart(spellRequest.getPage());
		}
		if (spellRequest.getSearch() != null) {
			input.getSearch().setValue(spellRequest.getSearch());
			//input.getSearch().setRegex(true);
		}
		return repo.findAll(input, SpellApiDto::new).getData();
	}
}