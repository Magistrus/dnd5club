package club.dnd5.portal.controller.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.MetaApi;
import club.dnd5.portal.model.classes.HeroClass;
import club.dnd5.portal.model.image.ImageType;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.ImageRepository;
import club.dnd5.portal.repository.classes.ClassRepository;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Meta", description = "The meta API")
@RestController
public class MetaApiController {
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private SpellDatatableRepository spellRepository;
	@GetMapping(value = "/api/v1/meta/classes", produces = MediaType.APPLICATION_JSON_VALUE)
	public MetaApi getClassesMeta() {
		MetaApi meta = new MetaApi();
		meta.setTitle("Классы (Classes) D&D 5e");
		meta.setMenu("Классы");
		return meta;
	}

	@GetMapping(value = "/api/v1/meta/classes/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public MetaApi getClassMeta(@PathVariable String englishName) {
		HeroClass heroClass = classRepository.findByEnglishName(englishName.replace('_', ' '));
		MetaApi meta = new MetaApi();
		meta.setTitle(String.format("%s (%s) | Классы D&D 5e", heroClass.getCapitalazeName(), heroClass.getEnglishName()));
		meta.setDescription(String.format("%s (%s) - описание класса персонажа по D&D 5-редакции", heroClass.getCapitalazeName(), heroClass.getEnglishName()));
		meta.setMenu("Классы");
		Collection<String> images = imageRepository.findAllByTypeAndRefId(ImageType.CLASS, heroClass.getId());
		if (!images.isEmpty()) {
			meta.setImage(images.iterator().next());
		}
		return meta;
	}
	
	@GetMapping(value = "/api/v1/meta/classes/{classEnglishName}/{archetypeEnglishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public MetaApi getArchetypeMeta(String englishName, String archetypeEnglishName) {
		HeroClass heroClass = classRepository.findByEnglishName(englishName.replace('_', ' '));
		MetaApi meta = new MetaApi();
		meta.setTitle(String.format("%s (%s) | Классы D&D 5e", heroClass.getCapitalazeName(), heroClass.getEnglishName()));
		meta.setDescription(String.format("%s (%s) - описание класса персонажа по D&D 5-редакции", heroClass.getCapitalazeName(), heroClass.getEnglishName()));
		meta.setMenu("Классы");
		Collection<String> images = imageRepository.findAllByTypeAndRefId(ImageType.CLASS, heroClass.getId());
		if (!images.isEmpty()) {
			meta.setImage(images.iterator().next());
		}
		return meta;
	}
	
	@GetMapping(value = "/api/v1/meta/spells", produces = MediaType.APPLICATION_JSON_VALUE)
	public MetaApi getSpellsMeta() {
		MetaApi meta = new MetaApi();
		meta.setTitle("Заклинания (Spells) D&D 5e");
		meta.setDescription("Заклинания по D&D 5 редакции");
		meta.setMenu("Заклинания");
		return meta;
	}

	@GetMapping(value = "/api/v1/meta/spells/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public MetaApi getSpellMeta(@PathVariable String englishName) {
		Spell spell = spellRepository.findByEnglishName(englishName.replace('_', ' '));
		MetaApi meta = new MetaApi();
		meta.setTitle(String.format("%s (%s)", spell.getName(), spell.getEnglishName()) + " | Заклинания D&D 5e");
		meta.setDescription(String.format("%s %s, %s", (spell.getLevel() == 0 ? "Заговор" : spell.getLevel() + " уровень"), spell.getName(), spell.getSchool().getName()));
		meta.setMenu("Заклинания");
		meta.setImage(String.format("https://image.dnd5.club:8089/magic/%s.png", StringUtils.capitalize(spell.getSchool().name().toLowerCase())));
		meta.setKeywords(spell.getAltName());
		return meta;	
	}
}
