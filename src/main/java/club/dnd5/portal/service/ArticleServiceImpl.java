package club.dnd5.portal.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import club.dnd5.portal.model.articles.Article;
import club.dnd5.portal.model.articles.AtricleStatus;
import club.dnd5.portal.repository.ArticleRepository;

public class ArticleServiceImpl implements ArticleService {
	@Autowired
	private ArticleRepository repo;

	@Override
	public Collection<Article> findAllByStatus(AtricleStatus status) {
		return repo.findAllByStatus(status);
	}
}