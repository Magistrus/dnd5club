package club.dnd5.portal.dto.api.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor

@Getter
public class ClassTabAp {
	private String name;
	private String url;
	private String type;
	private int order;
	private boolean raw;
}
