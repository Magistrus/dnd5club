package club.dnd5.portal.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.model.foundary.FBeastiary;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;

@RestController
@RequestMapping("/api")
public class SpellApiConroller {
	@Autowired
	private SpellDatatableRepository repo;
	
	@CrossOrigin
	@GetMapping("/spells")
	public FBeastiary getSpells(){
		return null;
	}
}