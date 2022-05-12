package club.dnd5.portal.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
public class FilterValueApi {
    private String label; // отображаемое название
    private String key; // ключ для сохранения и запроса
	@JsonProperty("default")
    private Boolean defaultValue; // значение по-умолчанию
    private String tooltip; // подсказка при наведении

    public FilterValueApi(String label, String key, Boolean defaultValue) {
		this.label = label;
		this.key = key;
		this.defaultValue = defaultValue;
	}
}