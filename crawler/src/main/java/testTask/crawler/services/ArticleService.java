package testTask.crawler.services;

import testTask.crawler.enteties.Article;

import java.util.List;

public interface ArticleService {
    Article save(Article article);
    List<Article> findAll();
    Article parseFromWikiUrl(String url);
}
