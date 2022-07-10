package club.dnd5.portal.dto.api.wiki;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.model.god.Domain;
import club.dnd5.portal.model.god.God;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class GodDetailApi extends GodApi {
	private String description;
	private String rank;
	private Collection<String> titles;
	private String symbol;
	private Collection<String> domains;
	private Collection<String> panteons;
	private Collection<String> images;
	private SourceApi source;

	public GodDetailApi(God god) {
		super(god);
		url = null;
		description = god.getDescription();
		rank = String.format("%s %s", god.getRank(), god.getSex().getCyrilicName());
		if (god.getNicknames() != null) {
			titles = Arrays.asList(god.getNicknames().split(","));
		}
		symbol = god.getSymbol();
		domains = god.getDomains().stream().map(Domain::getCyrilicName).collect(Collectors.toList());
		panteons = Collections.singleton(god.getPantheon().getName());
		source = new SourceApi(god.getBook());
	}
}