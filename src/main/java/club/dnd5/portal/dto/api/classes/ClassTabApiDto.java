package club.dnd5.portal.dto.api.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor

@Getter
public class ClassTabApiDto {
	private String name;
	private String url;
	private String type;
	private int order;
	private boolean raw;
}
