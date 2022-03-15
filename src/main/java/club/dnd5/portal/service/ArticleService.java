package club.dnd5.portal.service;

import java.util.Collection;

import club.dnd5.portal.model.articles.Article;
import club.dnd5.portal.model.articles.AtricleStatus;

public interface ArticleService {
	public Collection<Article> findAllByStatus(AtricleStatus status);
}