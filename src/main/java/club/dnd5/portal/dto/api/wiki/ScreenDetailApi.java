package club.dnd5.portal.dto.api.wiki;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.dto.api.classes.NameApi;
import club.dnd5.portal.model.screen.Screen;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class ScreenDetailApi extends ScreenApi {
    private String description;
    private List<ScreenApi> chields;

    public ScreenDetailApi(Screen screen) {
        name = new NameApi(screen.getName(), screen.getEnglishName());
        url = String.format("/screens/%s", screen.getUrlName());
        order = screen.getOrdering();

        if (screen.getParent() != null) {
            source = new SourceApi(screen.getBook());
            description = screen.getDescription();
        } else {
            chields = screen.getChields()
                    .stream()
                    .map(ScreenApi::new)
                    .collect(Collectors.toList());
        }
    }
}
