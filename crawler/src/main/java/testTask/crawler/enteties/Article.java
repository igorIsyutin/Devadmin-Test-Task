package testTask.crawler.enteties;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Article {
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String filePath;

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", filePath='" + (filePath == null ? "no file" : filePath) + '\'' +
                '}';
    }
}
