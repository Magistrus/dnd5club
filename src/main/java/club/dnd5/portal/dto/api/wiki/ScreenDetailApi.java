package club.dnd5.portal.dto.api.wiki;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.dto.api.classes.NameApi;
import club.dnd5.portal.model.screen.Screen;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class ScreenDetailApi extends ScreenApi {
    private String description;
    private ScreenDetailApi parent;
    private List<ScreenApi> chields;

    public ScreenDetailApi(Screen screen) {
        name = new NameApi(screen.getName(), screen.getEnglishName());
        url = String.format("/screens/%s", screen.getUrlName());
        order = screen.getOrdering();

        if (screen.getParent() != null) {
            source = new SourceApi(screen.getBook());
            description = screen.getDescription();
            parent = new ScreenDetailApi(screen.getParent());
            parent.setDescription(null);
            parent.setChields(null);
        } else {
            chields = screen.getChields()
                    .stream()
                    .map(ScreenApi::new)
                    .collect(Collectors.toList());
        }
    }
}
