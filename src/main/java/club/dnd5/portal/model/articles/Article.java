package club.dnd5.portal.model.articles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.boot.json.JacksonJsonParser;

import club.dnd5.portal.model.user.User;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "articles")
public class Article {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;
	private String description;
	@Column(columnDefinition = "TEXT")
	private String text = "";
	@Enumerated(EnumType.STRING)
	private ArtricleStatus status;
	private boolean linkAccess;
	private String tags;
	private String author;
	private String translation;
	private String originalAuthor;
	private String originalUrl;
	private String originalName;
	private String imageUrl;
	private String imageAuthor;
	private String cause_canceled;
	
	@ManyToOne
	@JoinColumn(name = "creator_id")
	private User creator;
	@ManyToOne
	@JoinColumn(name = "moderator_id")
	private User moderator;
	
	private LocalDateTime created;
	private LocalDateTime changed;
	private LocalDateTime moderated;
	private LocalDateTime published;
	private LocalDateTime deleted;
	
	@SuppressWarnings("unchecked")
	public String getShortText() {
		if (StringUtils.isEmpty(text)) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		JacksonJsonParser parser = new JacksonJsonParser();
		List<Map<String, Object>> result =  (List<Map<String, Object>>) parser.parseMap(text).getOrDefault("blocks", "");
		for (Map<String, Object> map : result) {
			if (map.get("type").equals("paragraph")) {
				Map<String, Object> m = ((Map<String, Object>) map.get("data"));
				builder.append(m.get("text"));
				builder.append(" ");
			}
		}
		return builder.toString();
	}

	public String getPublishedDate() {
		return published == null ? "" : published.format(formatter);
	}
	
	public String getLastDate() {
		if (published != null) {
			return getPublishedDate();
		}
		if (changed != null) {
			return changed == null ? "" : changed.format(formatter);
		}
		if (moderated != null) {
			return moderated == null ? "" : moderated.format(formatter);
		}
		if (deleted != null) {
			return deleted == null ? "" : deleted.format(formatter);
		}
		return created == null ? "" : created.format(formatter);
	}
}