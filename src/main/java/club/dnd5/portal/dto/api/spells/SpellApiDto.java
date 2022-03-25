package club.dnd5.portal.dto.api.spells;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.splells.Spell;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class SpellApiDto implements Serializable {
	private static final long serialVersionUID = 6266015163866595679L;
    public String name;
    public String englishName;
    public String altName;
    public byte level;
    public String school;
    @JsonProperty("time")
    public List<Timecast> timecast;
    public Range range;
    public Components components;
    //public List<Duration> duration;
    //public Classes classes;
    public String source;
    public List<String> entries;
    public Integer page;
    public List<String> damageInflict;
    public List<String> savingThrow;
    
	public SpellApiDto(Spell spell) {
		this.name = StringUtils.capitalize(spell.getName().toLowerCase());
		this.englishName = spell.getEnglishName();
		this.level = spell.getLevel();
		this.school = spell.getSchool().name().toLowerCase();
		this.source = spell.getBook().getSource();
		if (!spell.getDamageType().isEmpty()) {
			this.damageInflict = spell.getDamageType().stream().map(DamageType::name).map(String::toLowerCase).collect(Collectors.toList());  
		}
		components = new Components();
		if (spell.getVerbalComponent()) {
			components.setV(true);
		}
		if (spell.getSomaticComponent()) {
			components.setS(true);
		}
		if (spell.getAdditionalMaterialComponent()!=null) {
			components.setM(spell.getAdditionalMaterialComponent());
		}
		this.range = new Range(spell);
		this.timecast = spell.getTimes().stream().map(Timecast::new).collect(Collectors.toList());
		this.entries = new ArrayList<String>(2);
		this.entries.addAll(Arrays.stream(spell.getDescription().replace("<p>", "").split("</p>")).map(t->t.replace("\\\"", "")).filter(t -> !t.isEmpty()).collect(Collectors.toList()));
	}
}