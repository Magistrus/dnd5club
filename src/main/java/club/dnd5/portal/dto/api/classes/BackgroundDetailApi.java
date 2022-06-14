package club.dnd5.portal.dto.api.classes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.model.background.Background;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class BackgroundDetailApi extends BackgroundApi{
	private String url;
	private SourceApi source;

	public BackgroundDetailApi(Background background) {
		super(background);
		url = String.format("/backgrounds/fragment/%d", background.getId());
		source = new SourceApi(background.getBook());
	}
}