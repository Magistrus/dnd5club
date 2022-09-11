package club.dnd5.portal.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.MetaApi;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Meta", description = "The meta API")
@RestController
public class MetaApiController {
	@Autowired
	private SpellDatatableRepository spellRepository;
	
	@GetMapping(value = "/api/v1/meta/spells", produces = MediaType.APPLICATION_JSON_VALUE)
	public MetaApi getSpellsMeta() {
		MetaApi meta = new MetaApi();
		meta.setTitle("Заклинания (Spells) D&D 5e");
		meta.setDescription("Заклинания по D&D 5 редакции");
		meta.setMenu("Заклинания");
		return meta;
	}

	@GetMapping(value = "/api/v1/meta/spells/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public MetaApi getSpellMeta(String englishName) {
		Spell spell = spellRepository.findByEnglishName(englishName.replace('_', ' '));
		MetaApi meta = new MetaApi();
		meta.setTitle(String.format("%s (%s)", spell.getName(), spell.getEnglishName()) + " | Заклинания D&D 5e");
		meta.setDescription(String.format("%s %s, %s", (spell.getLevel() == 0 ? "Заговор" : spell.getLevel() + " уровень"), spell.getName(), spell.getSchool().getName()));
		meta.setMenu("Заклинания");
		meta.setImage(String.format("https://image.dnd5.club:8089/magic/%s.png", StringUtils.capitalize(spell.getSchool().name().toLowerCase())));
		return meta;	}
}
