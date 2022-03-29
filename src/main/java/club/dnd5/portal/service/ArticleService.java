package club.dnd5.portal.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import club.dnd5.portal.model.articles.Article;
import club.dnd5.portal.model.articles.ArtricleStatus;
import club.dnd5.portal.model.user.User;

public interface ArticleService {
	Optional<Article> findById(Integer id);
	
	Collection<Article> findAllByStatus(ArtricleStatus status, Sort sort);
	Collection<Article> findAllByCreator(User user);

	Article save(Article article, User creator);

	long getCountArticlesByUser(User user);
	long getCountByStatus(ArtricleStatus status);
	long getCountByUserAndStatus(User user, ArtricleStatus status);
}