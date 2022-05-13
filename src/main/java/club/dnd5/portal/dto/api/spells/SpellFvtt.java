package club.dnd5.portal.dto.api.spells;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.DamageType;
import club.dnd5.portal.model.splells.Spell;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class SpellFvtt implements Serializable {
	private static final long serialVersionUID = 6266015163866595679L;
	private String name;
    private String englishName;
    private String altName;
    private byte level;
    private String school;
    @JsonProperty("time")
    private List<Timecast> timecast;
    private Range range;
    private Components components;
    private List<Duration> duration;
    private Classes classes;
    private String source;
    private List<String> entries;
    private Short page;
    private List<String> damageInflict;
    private List<String> savingThrow;
    private EntriesHigherLevel entriesHigherLevel;
    private Meta meta;

	public SpellFvtt(Spell spell) {
		this.name = StringUtils.capitalize(spell.getName().toLowerCase());
		this.englishName = spell.getEnglishName();
		this.level = spell.getLevel();
		this.school = spell.getSchool().name().toLowerCase();
		this.source = spell.getBook().getSource();
		if (spell.getPage() != null) {
			this.page = spell.getPage();
		}
		if (!spell.getDamageType().isEmpty()) {
			this.damageInflict = spell.getDamageType()
					.stream()
					.map(DamageType::name)
					.map(String::toLowerCase)
					.collect(Collectors.toList());
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
		String durationTetx = spell.getDuration();
		duration = Arrays.stream(durationTetx.split(" или ")).map(Duration::new).collect(Collectors.toList());
		this.timecast = spell.getTimes().stream().map(Timecast::new).collect(Collectors.toList());
		this.entries = new ArrayList<String>(2);
		this.entries.addAll(Arrays.stream(spell.getDescription().replace("<p>", "").split("</p>")).map(t->t.replace("\\\"", "")).filter(t -> !t.isEmpty()).collect(Collectors.toList()));
		this.classes = new Classes(spell.getHeroClass());
		if (spell.getRitual()) {
			meta = new Meta();
			meta.setRitual(true);
		}
		if (spell.getUpperLevel() != null) {
			this.entriesHigherLevel = new EntriesHigherLevel(spell.getUpperLevel());
		}
	    Matcher matcher = Pattern.compile("(<span class=\"saving_throw\">\\W+<\\/span>)+").matcher(spell.getDescription());
	    if(matcher.matches()) {
	    	this.savingThrow = new ArrayList<>(matcher.groupCount());
	    	while (matcher.find()) {
				String ability = AbilityType.parse(matcher.group()).getCapitalizeName().toLowerCase();
				savingThrow.add(ability);
			}
	    }
	}
}