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
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.spell.SpellApi;
import club.dnd5.portal.dto.api.spells.SpellFvtt;
import club.dnd5.portal.dto.api.spells.SpellsFvtt;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;

@RestController
public class SpellApiConroller {
	@Autowired
	private SpellDatatableRepository repo;
	
	@PostMapping(value = "/api/v1/spells", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SpellApi> getSpells() {
		DataTablesInput input = new DataTablesInput();
		input.setLength(-1);
		return repo.findAll(input).getData().stream()
				.map(SpellApi::new)
				.collect(Collectors.toList());
	}
	
	@CrossOrigin
	@GetMapping(value = "/api/fvtt/v1/spells", produces = MediaType.APPLICATION_JSON_VALUE)
	public SpellsFvtt getSpells(String search, String exact){
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
		columns.add(column);
		input.setColumns(columns);
		input.setLength(-1);
		Specification<Spell> specification = null;
		if (search != null) {
			if (exact != null) {
				specification = (root, query, cb) -> cb.equal(root.get("name"), search.trim().toUpperCase());
			} else {
				input.getSearch().setValue(search);
				input.getSearch().setRegex(Boolean.FALSE);
			}
		}
		return new SpellsFvtt(repo.findAll(input, specification, specification, SpellFvtt::new).getData());
	}
}