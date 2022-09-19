package club.dnd5.portal.model.foundary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FSave {
    private String ability = "";
    private Object dc; // new FDC
    private String scaling = "spell"; //str
}