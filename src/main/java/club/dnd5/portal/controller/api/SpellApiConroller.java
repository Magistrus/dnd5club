package club.dnd5.portal.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.Search;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Optional;

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
	public List<SpellApiDto> getSpells(@RequestBody SpellRequestDto request){
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
		if (request.getFilter() != null && request.getFilter().getLevels() != null) {
			Search search = new Search();
			search.setValue(String.join("|", request.getFilter().getLevels().stream().map(String::valueOf).collect(Collectors.toList())));
			column.setSearch(search);
		}
		columns.add(column);
		input.setColumns(columns);
		if (request.getLimit() != Integer.MAX_VALUE) {
			input.setLength(request.getLimit());
		}
		if (request.getPage()!= null) {
			input.setStart(request.getPage());
		}
		if (request.getSearch() != null) {
			input.getSearch().setValue(request.getSearch());
			//input.getSearch().setRegex(true);
		}
		return repo.findAll(input, SpellApiDto::new).getData();
	}
}