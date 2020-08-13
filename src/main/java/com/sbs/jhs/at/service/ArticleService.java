package com.sbs.jhs.at.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.jhs.at.dao.ArticleDao;
import com.sbs.jhs.at.dto.Article;
import com.sbs.jhs.at.util.Util;

@Service
public class ArticleService {
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private FileService fileService;

	// 리스트
	public List<Article> getForPrintArticles() {
		List<Article> articles = articleDao.getForPrintArticles();

		return articles;
	}
	
	// 디테일
	public Article getForPrintArticleById(int id) {
		Article article = articleDao.getForPrintArticleById(id);

		return article;
	}

	// 글쓰기
	public int write(Map<String, Object> param) {
		articleDao.write(param);
		int id = Util.getAsInt(param.get("id"));
		System.out.println("paramId : " + id);
		
		String fileIdsStr = (String) param.get("fileIdsStr");
		System.out.println("write fileIdsStr : " + fileIdsStr);
		
		if (fileIdsStr != null && fileIdsStr.length() > 0) {
			List<Integer> fileIds = Arrays.asList(fileIdsStr.split(",")).stream().map(s -> Integer.parseInt(s.trim()))
					.collect(Collectors.toList());

			// 파일이 먼저 생성된 후에, 관련 데이터가 생성되는 경우에는, file의 relId가 일단 0으로 저장된다.
			// 그것을 뒤늦게라도 이렇게 고처야 한다.
			for (int fileId : fileIds) {
				fileService.changeRelId(fileId, id);
			}
		}

		return id;
	}
	

	// 글 갯수
	public int getForPrintListArticlesCount(String searchKeyword) {
		return articleDao.getForPrintListArticlesCount(searchKeyword);
	}

	// 검색 글 리스트
	public List<Article> getForPrintArticles(int nowPage, int itemsInAPage, String searchKeyword) {
		int limitFrom = (nowPage - 1) * itemsInAPage;
		return articleDao.getForPrintArticles(limitFrom, itemsInAPage, searchKeyword);
	}

	// 글 수정을 위한, 본인 확인
	public Map<String, Object> getArticleModifyAvailable(int id, int actorMemberId) {
		Article article = getForPrintArticleById(id);

		Map<String, Object> rs = new HashMap<>();

		if (article.getMemberId() == actorMemberId) {
			rs.put("resultCode", "S-1");
			rs.put("msg", "수정권한이 있습니다.");
		} else {
			rs.put("resultCode", "F-1");
			rs.put("msg", "수정권한이 없습니다.");
		}

		return rs;
	}

	// 글 수정
	public Map<String, Object> modify(Map<String, Object> param) {
		articleDao.modifyArticle(param);
		int id = Util.getAsInt(param.get("id"));
		Map<String, Object> rs = new HashMap<>();

		rs.put("resultCode", "S-1");
		rs.put("msg", String.format("%d번 게시물이 생성되었습니다.", id));
		rs.put("msg", String.format("%d번 게시물이 수정되었습니다.", id));

		return rs;
	}

	// 글 삭제를 위한, 본인 확인
	public Map<String, Object> getArticleDeleteAvailable(int id, int actorMemberId) {
		Map<String, Object> rs = getArticleModifyAvailable(id, actorMemberId);

		String msg = (String) rs.get("msg");
		msg = msg.replace("수정", "삭제");
		rs.put("msg", msg);

		return rs;
	}

	// 글 삭제
	public Map<String, Object> deleteArticle(int id) {
		articleDao.deleteArticle(id);
		Map<String, Object> rs = new HashMap<>();
		rs.put("resultCode", "S-1");
		rs.put("msg", String.format("%d번 게시물이 삭제되었습니다.", id));
		return rs;
	}

	public int getArticleMaxCount() {
		return articleDao.getArticleMaxCount();
	}

}
