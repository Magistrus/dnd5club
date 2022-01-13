package club.dnd5.portal.model.foundary.data.details;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FType {
    public String value;
    public String subtype;
    public String swarm;
    public String custom;
}