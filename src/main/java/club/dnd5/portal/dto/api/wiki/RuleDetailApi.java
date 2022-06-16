package club.dnd5.portal.dto.api.wiki;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.rule.Rule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class RuleDetailApi extends RuleApi {
	private String description;
	private String type;
	public RuleDetailApi(Rule rule) {
		super(rule);
		url = null;
		description = rule.getDescription();
		type = rule.getType(); 
	}
}