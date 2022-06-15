package club.dnd5.portal.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)


@NoArgsConstructor
@Getter
@Setter
public class NameValueApi {
	private String name;
	private String shortname;
	private Object value;
	private Object additional;
	public NameValueApi(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	public NameValueApi(String name, String shortName, Object value) {
		this.name = name;
		this.value = value;
		this.shortname = shortName;
	}

}