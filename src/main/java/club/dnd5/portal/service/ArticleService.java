package club.dnd5.portal.service;

import java.util.Collection;
import java.util.Optional;

import club.dnd5.portal.model.articles.Article;
import club.dnd5.portal.model.articles.AtricleStatus;
import club.dnd5.portal.model.user.User;

public interface ArticleService {
	Optional<Article> findById(Integer id);
	
	Collection<Article> findAllByStatus(AtricleStatus status);
	Collection<Article> findAllByCreator(User user);

	Article save(Article article, User creator);
	
}