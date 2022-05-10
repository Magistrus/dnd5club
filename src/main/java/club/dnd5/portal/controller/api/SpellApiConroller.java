package club.dnd5.portal.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.Search;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.spells.SpellApiDto;
import club.dnd5.portal.dto.api.spells.SpellRequestDto;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;

@RestController
@RequestMapping("/api")
public class SpellApiConroller {
	@Autowired
	private SpellDatatableRepository repo;
	
	@GetMapping(value = "/api/v1/spells", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SpellApiDto> getSpells() {
		DataTablesInput input = new DataTablesInput();
		input.setLength(-1);
		return repo.findAll(input).getData().stream()
				.map(SpellApiDto::new)
				.collect(Collectors.toList());
	}
	
	@CrossOrigin
	@PostMapping(value = "/v1/spells", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SpellApiDto> getSpells(@RequestBody SpellRequestDto request){
		DataTablesInput input = new DataTablesInput();
		List<Column> columns = new ArrayList<Column>(3);
		Column column = new Column();
		column.setData("name");
		column.setName("name");
		column.setSearchable(Boolean.TRUE);
		column.setOrderable(Boolean.TRUE);
		column.setSearch(new Search("", Boolean.FALSE));
		columns.add(column);
		
		column = new Column();
		column.setData("englishName");
		column.setName("englishName");
		column.setSearch(new Search("", Boolean.FALSE));
		column.setSearchable(Boolean.TRUE);
		column.setOrderable(Boolean.TRUE);
		columns.add(column);
		
		column = new Column();
		column.setData("level");
		column.setName("level");
		column.setSearch(new Search("", Boolean.FALSE));
		column.setSearchable(Boolean.FALSE);
		column.setOrderable(Boolean.TRUE);
		
		if (request.getFilter() != null && request.getFilter().getLevels() != null) {
			Search search = new Search();
			search.setValue(String.join("|", request.getFilter().getLevels().stream().map(String::valueOf).collect(Collectors.toList())));
			column.setSearch(search);
		}
		columns.add(column);
		input.setColumns(columns);
		input.setLength(request.getLimit());
		
		if (request.getPage() != null) {
			input.setStart(request.getPage());
		}
		Specification<Spell> specification = null;
		//input.setOrder(Arrays.asList(new Order(0, "asc")));
		if (request.getSearch() != null) {
			if (request.getSearch().getExact() != null && request.getSearch().getExact()) {
				specification = (root, query, cb) -> cb.equal(root.get("name"), request.getSearch().getValue().trim().toUpperCase());
			} else {
				input.getSearch().setValue(request.getSearch().getValue());
				input.getSearch().setRegex(Boolean.FALSE);
			}
		}
		return repo.findAll(input, specification, specification, SpellApiDto::new).getData();
	}
}