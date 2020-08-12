package com.sbs.jhs.at.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.jhs.at.dto.Article;
import com.sbs.jhs.at.dto.Reply;

@Mapper
public interface ArticleDao {
	List<Article> getForPrintArticles();

	Article getForPrintArticleById(@Param("id") int id);

	void write(Map<String, Object> param);

	int getForPrintListArticlesCount(String searchKeyword);

	List<Article> getForPrintArticles(int limitFrom, int itemsInAPage, String searchKeyword);

	void modifyArticle(Map<String, Object> param);

	void deleteArticle(int id);

	int getArticleMaxCount();
}
