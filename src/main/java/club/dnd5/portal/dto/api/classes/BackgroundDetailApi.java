package club.dnd5.portal.dto.api.classes;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.model.SkillType;
import club.dnd5.portal.model.background.Background;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class BackgroundDetailApi extends BackgroundApi {
	private String url;
	private Collection<String> skills;
	private String toolOwnership;
	private Collection<String> languages;
	private Collection<String> equipments;
	private Integer startGold;
	private String description;
	private String personalization;
	
	private SourceApi source;

	public BackgroundDetailApi(Background background) {
		super(background);
		url = String.format("/backgrounds/fragment/%d", background.getId());
		skills = background.getSkills().stream().map(SkillType::getCyrilicName).collect(Collectors.toList());
		if (background.getOtherSkills() != null) {
			skills.add(background.getOtherSkills());
		}
		toolOwnership = background.getToolOwnership();
		if (background.getEquipmentsText() != null) {
			equipments = Arrays.asList(background.getEquipmentsText().split(", "));
		}
		startGold = background.getStartMoney();
		description = background.getDescription();
		if (background.getPersonalization() != null) {
			personalization = background.getPersonalization();
		}
		source = new SourceApi(background.getBook());
	}
}