package testTask.crawler.repositories;

import org.springframework.data.repository.CrudRepository;
import testTask.crawler.enteties.Article;

import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
    Optional<Article> findByTitle(String title);
}
