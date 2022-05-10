package club.dnd5.portal.controller.api;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.foundary.FBeastiary;
import club.dnd5.portal.model.fvtt.plutonium.FBeast;
import club.dnd5.portal.repository.datatable.BestiaryDatatableRepository;

@RestController
public class BestiaryApiConroller {
	@Autowired
	private BestiaryDatatableRepository repo;
	
	@CrossOrigin
	@GetMapping("/api/fvtt/v1/bestiary")
	public FBeastiary getCreatures(){
		List<FBeast> list = ((Collection<Creature>) repo.findAll()).stream().map(FBeast::new).collect(Collectors.toList());
		return new FBeastiary(list);
	}
}