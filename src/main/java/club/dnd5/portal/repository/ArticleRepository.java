package club.dnd5.portal.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.articles.Article;
import club.dnd5.portal.model.articles.AtricleStatus;
import club.dnd5.portal.model.user.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer>{
	Collection<Article> findAllByStatusOrderByPublishedDesc(AtricleStatus status);

	Collection<Article> findAllByCreator(User user);
}