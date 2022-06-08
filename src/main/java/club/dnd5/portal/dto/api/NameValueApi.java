package club.dnd5.portal.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NameValueApi {
	 @NonNull private String name;
	 @NonNull private Object value;
	 private Boolean hover;
	 private Boolean radius;
}