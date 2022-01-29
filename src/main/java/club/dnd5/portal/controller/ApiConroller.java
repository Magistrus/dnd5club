package club.dnd5.portal.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.foundary.FCreature;
import club.dnd5.portal.repository.datatable.BestiaryDatatableRepository;

@RestController
@RequestMapping("/api")
public class ApiConroller {
	@Autowired
	private BestiaryDatatableRepository repo;
	
	@GetMapping("/beastiary")
	public List<FCreature> getCreatures(){
		return ((Collection<Creature>) repo.findAll()).stream().map(FCreature::new).collect(Collectors.toList());
	}
}