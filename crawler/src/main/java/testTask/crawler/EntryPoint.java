package testTask.crawler;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import testTask.crawler.enteties.Article;
import testTask.crawler.services.ArticleService;

import java.util.Scanner;

@Service
public class EntryPoint implements CommandLineRunner {
    private ArticleService articleService;

    //autowire ArticleService bean
    public EntryPoint(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public void run(String... args) {
        //create scanner to get data from cmd
        Scanner in = new Scanner(System.in);
        boolean loop = true;
        //loop asking new urls
        while (loop) {
            System.out.println("Enter url");
            //get url from cmd
            String url = in.nextLine();
            //create article
            Article article = articleService.parseFromWikiUrl(url);
            //save article
            articleService.save(article);
            //output info
            System.out.println("Created article: " + article);
            System.out.println("Articles in db: " + articleService.findAll());
            //ask to continue
            System.out.println("Continue y/n?");
            //fast iml. Only y continue
            loop = in.nextLine().equals("y");
        }
    }
}
