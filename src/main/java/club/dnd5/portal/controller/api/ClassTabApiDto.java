package club.dnd5.portal.controller.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor

@Getter
public class ClassTabApiDto {
	private String name;
	private String url;
	private String icon;
	private int order;
	private boolean raw;
}