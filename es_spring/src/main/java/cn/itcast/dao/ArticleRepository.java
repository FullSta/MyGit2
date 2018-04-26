package cn.itcast.dao;

import cn.itcast.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ArticleRepository extends ElasticsearchRepository<Article,Integer> {

    List<Article> findByTitle(String title);

    Page<Article> findByTitle(String title, Pageable pageable);
}
