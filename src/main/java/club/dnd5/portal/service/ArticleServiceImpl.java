package club.dnd5.portal.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import club.dnd5.portal.model.articles.Article;
import club.dnd5.portal.model.articles.AtricleStatus;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.ArticleRepository;

@Service
public class ArticleServiceImpl implements ArticleService {
	@Autowired
	private ArticleRepository repo;

	@Override
	public Collection<Article> findAllByStatus(AtricleStatus status) {
		return repo.findAllByStatus(status);
	}

	@Override
	public Optional<Article> findById(Integer id) {
		return repo.findById(id);
	}

	@Transactional
	@Override
	public Article save(Article article, User creator) {
		article.setCreated(LocalDateTime.now());
		article.setCreator(creator);
		article.setStatus(AtricleStatus.CREATED);
		return repo.save(article);
	}

	@Override
	public Collection<Article> findAllByCreator(User user) {
		return repo.findAllByCreator(user);
	}
}