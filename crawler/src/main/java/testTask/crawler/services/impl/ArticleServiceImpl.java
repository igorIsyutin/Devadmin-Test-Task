package testTask.crawler.services.impl;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import testTask.crawler.enteties.Article;
import testTask.crawler.repositories.ArticleRepository;
import testTask.crawler.services.ArticleService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {
    private ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**
     * Check if article with such title exist already. If yes update it, else save new
     *
     * @param article article to create or update
     * @return saved article
     */
    @Override
    public Article save(Article article) {
        if (article == null) return null;
        //search if article with such title exist
        Optional<Article> optionalArticle = articleRepository.findByTitle(article.getTitle());
        //update if existed
        optionalArticle.ifPresent(article1 -> article.setId(article1.getId()));
        return articleRepository.save(article);
    }

    /**
     * @return all articles in db
     */
    @Override
    public List<Article> findAll() {
        Iterable<Article> iterable = articleRepository.findAll();
        List<Article> articles = new ArrayList<>();
        iterable.forEach(articles::add);
        return articles;
    }

    /**
     * This method try to connect to URL. Search title of article and set it. Check if article have img. If yes download it and set absolute path to it.
     *
     * @param url an absolute URL to the wiki article
     * @return article parsed from URL
     */
    @Override
    public Article parseFromWikiUrl(String url) {
        try {
            //get Html page
            Document doc = Jsoup.connect(url).get();
            //get body of page
            Element body = doc.body();

            //get title by its Id
            Element firstHeading = body.getElementById("firstHeading");
            String title = firstHeading.text();
            Article article = new Article();
            article.setTitle(title);

            //search if page contains infobox
            Elements infobox = body.getElementsByClass("infobox vcard");
            if (infobox.size() > 0) {
                //get first founded infobox
                Element element = infobox.get(0);
                //search if infobox contains img
                Elements imgs = element.getElementsByTag("img");
                if (imgs.size() > 0) {
                    //get first img in box and set absolute path
                    String imgSrc = imgs.get(0).absUrl("src");
                    File img = new File(imgSrc.substring(imgSrc.lastIndexOf("/") + 1));
                    FileUtils.copyURLToFile(new URL(imgSrc), img);
                    article.setFilePath(img.getAbsolutePath());
                }
            }
            return article;
        } catch (IOException e) {
            //handle exception
            System.out.println("Can not connect to url");
            System.out.println(e.getMessage());
        }
        return null;
    }
}
