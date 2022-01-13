package club.dnd5.portal.model.foundary.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FMaterials {
    public String value;
    public boolean consumed;
    public int cost;
    public int supply;
}