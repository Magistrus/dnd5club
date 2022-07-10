package club.dnd5.portal.dto.api.wiki;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.dto.api.classes.NameApi;
import club.dnd5.portal.model.rule.Rule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class RuleApi {
	protected NameApi name;
	protected String url;
	protected SourceApi source;

	public RuleApi(Rule rule) {
		name = new NameApi(rule.getName(), rule.getEnglishName());
		url = String.format("/rules/%s", rule.getUrlName());
		source = new SourceApi(rule.getBook());
	}
}
