package cn.itcast.service;

import cn.itcast.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {
    void save(Article article);

    void delete(Article article);

    public Article findOne(Integer id);

    public Iterable<Article> findAll();

    public Page<Article> findAll(Pageable pageable);

    public List<Article> findByTitle(String title);

    public Page<Article> findByTitle(String title, Pageable pageable);
}
