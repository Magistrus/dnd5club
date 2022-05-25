package club.dnd5.portal.dto.api.classes;

import club.dnd5.portal.dto.api.RequestApi;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OptionRequesApi extends RequestApi {
    public OptionFilter filter;
}