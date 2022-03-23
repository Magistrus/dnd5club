package club.dnd5.portal.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import javax.transaction.Transactional;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import club.dnd5.portal.model.articles.Article;
import club.dnd5.portal.model.articles.ArtricleStatus;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.ArticleRepository;

@Service
public class ArticleServiceImpl implements ArticleService {
	@Autowired
	private ArticleRepository repo;

	@Override
	public Collection<Article> findAllByStatus(ArtricleStatus status, Sort sort) {
		return repo.findAllByStatusOrderByPublishedDesc(status);
	}

	@Override
	public Optional<Article> findById(Integer id) {
		return repo.findById(id);
	}

	@Transactional
	@Override
	public Article save(Article article, User user) {
		if (article.getCreated() == null) {
			article.setCreated(LocalDateTime.now());
		}
		else {
			article.setChanged(LocalDateTime.now());
		}
		if (article.getStatus() == ArtricleStatus.PUBLISHED) {
			article.setModerated(LocalDateTime.now());
			article.setModerator(user);
		}
		else {
			article.setCreator(user);
		}
		if (article.getId() == null) {
			article.setStatus(ArtricleStatus.CREATED);	
		}
		article.setText(Jsoup.clean(article.getText(), Safelist.basic()));
		return repo.save(article);
	}

	@Override
	public Collection<Article> findAllByCreator(User user) {
		return repo.findAllByCreatorOrderByCreatedDesc(user);
	}

	@Override
	public long getCountArticlesByUser(User user) {
		return repo.countByCreator(user);
	}

	@Override
	public long getCountByStatus(ArtricleStatus status) {
		return repo.countByStatus(status);
	}
}