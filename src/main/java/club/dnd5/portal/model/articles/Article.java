package club.dnd5.portal.model.articles;

import java.time.LocalDateTime;

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

import club.dnd5.portal.model.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "articles")
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;
	private String description;
	@Column(columnDefinition = "TEXT")
	private String text;
	@Enumerated(EnumType.STRING)
	private AtricleStatus status;
	private String author;
	private String translation;
	private String originalAuthor;
	private String originalUrl;
	private String originalName;
	@ManyToOne
	@JoinColumn(name = "creator_id")
	private User creator;
	@ManyToOne
	@JoinColumn(name = "moderator_id")
	private User moderator;
	
	private LocalDateTime created;
	private LocalDateTime moderated;
	private LocalDateTime published;
	private LocalDateTime deleted;
}
