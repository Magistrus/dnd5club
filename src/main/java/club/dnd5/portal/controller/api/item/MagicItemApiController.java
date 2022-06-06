package club.dnd5.portal.controller.api.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.item.WeaponApi;
import club.dnd5.portal.repository.datatable.WeaponDatatableRepository;

@RestController
public class MagicItemApiController {
	@Autowired
	private WeaponDatatableRepository repo;
	
	@PostMapping(value = "/api/v1/weapons", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<WeaponApi> getWeapons(){
		return null;
	}
}