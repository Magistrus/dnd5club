package club.dnd5.portal.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.foundary.FBeast;
import club.dnd5.portal.model.foundary.FBeastiary;
import club.dnd5.portal.repository.datatable.BestiaryDatatableRepository;

@RestController
@RequestMapping("/api")
public class ApiConroller {
	@Autowired
	private BestiaryDatatableRepository repo;
	
	@CrossOrigin
	@GetMapping("/beastiary")
	public FBeastiary getCreatures(){
		List<FBeast> list = ((Collection<Creature>) repo.findAll()).stream().map(FBeast::new).collect(Collectors.toList());
		return new FBeastiary(list);
	}
}