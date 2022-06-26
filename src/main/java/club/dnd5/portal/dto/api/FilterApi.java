package club.dnd5.portal.dto.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class FilterApi {
	private String name; // название блока фильтра
	private String key; // ключ для сохранения и запроса
	private Boolean expand;
	private List<FilterApi> sources;
	private List<FilterApi> other;
	private List<FilterApi> custom;
		private List<FilterValueApi> values;
	
	public FilterApi(String key) {
		this.key = key;
	}
	
	public FilterApi(String name , String key) {
		this.name = name;
		this.key = key;
	}
}