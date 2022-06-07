package club.dnd5.portal.controller.api.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.item.WeaponApi;
import club.dnd5.portal.repository.datatable.MagicItemDatatableRepository;

@RestController
public class MagicItemApiController {
	@Autowired
	private MagicItemDatatableRepository repo;
	
	@PostMapping(value = "/api/v1/magic/items", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<WeaponApi> getItems(){
		return null;
	}
}