package club.dnd5.portal.util;

public final class HtmlConverter {
	public static String toHtml(String text) {
		if (text == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (String line : text.split("\\r")) {
			builder.append("<p>");
			builder.append(line);
			builder.append("</p>");
		}
		return builder.toString();
	}
}