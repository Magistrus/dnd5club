package club.dnd5.portal.model.foundary;

import java.util.List;

import club.dnd5.portal.model.foundary.token.FFlags;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FEffect {
    public String _id;
    public FFlags flags;
    public List<FChange> changes;
    public boolean disabled;
    public FDuration duration;
    public String icon;
    public String label;
    public String origin;
    public Object tint;
    public boolean transfer;
}
