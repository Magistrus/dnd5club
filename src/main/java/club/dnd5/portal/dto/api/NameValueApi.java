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
	private Object value;
	private Boolean hover;
	private Boolean radius;
	public NameValueApi(String name, Object value) {
		this.name = name;
		this.value = value;
	}
}