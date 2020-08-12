package com.sbs.jhs.at.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sbs.jhs.at.dto.Article;
import com.sbs.jhs.at.dto.ResultData;
import com.sbs.jhs.at.service.ArticleService;

@Controller
public class ArticleController {
	@Autowired
	private ArticleService articleService;

	@RequestMapping("/usr/article/list")
	public String showList(Model model, @RequestParam Map<String, Object> param) {
		String page = "1";
		if (param.get("page") != null && param.get("page").toString().length() != 0) {
			page = (String) param.get("page");
		}
		String searchKeyword = "";
		if (param.get("searchKeyword") != null && param.get("searchKeyword").toString().length() != 0) {
			searchKeyword = (String) param.get("searchKeyword");
		}	
		
		int itemsInAPage = 10;
		int totalCount = articleService.getForPrintListArticlesCount(searchKeyword);
		int totalPage = (int) Math.ceil(totalCount / (double) itemsInAPage);

		int nowPage = Integer.parseInt(page);
		int intPage = Integer.parseInt(page);

		if (intPage % 5 != 0) {
			intPage = intPage / 5;
			intPage = (intPage * 5) + 1;
		} else if (intPage % 5 == 0) {
			intPage = intPage - 4;
		}
		model.addAttribute("page", intPage);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("totalPage", totalPage);
		
		
		List<Article> articles = articleService.getForPrintArticles(nowPage, itemsInAPage, searchKeyword);

		model.addAttribute("articles", articles);

		return "article/list";
	}

	@RequestMapping("/usr/article/detail")
	public String showDetail(Model model, @RequestParam Map<String, Object> param) {
		int id = Integer.parseInt((String) param.get("id"));

		Article article = articleService.getForPrintArticleById(id);

		model.addAttribute("article", article);

		return "article/detail";
	}

	@RequestMapping("/usr/article/write")
	public String showWrite(Model model) {
		int articleMaxId = articleService.getArticleMaxCount() + 1;
		
		System.out.println("articleMaxId : " + articleMaxId);
		
		model.addAttribute("articleMaxId", articleMaxId);
		
		return "article/write";
	}

	@RequestMapping("/usr/article/doWrite")
	public ResultData doWrite(@RequestParam Map<String, Object> param, HttpServletRequest request) {
		Map<String, Object> rsDataBody = new HashMap<>();

		param.put("memberId", request.getAttribute("loginedMemberId"));

		int newArticleId = articleService.write(param);
		rsDataBody.put("replyId", newArticleId);
		

		String redirectUrl = (String) param.get("redirectUrl");
		redirectUrl = redirectUrl.replace("#id", newArticleId + "");
		//rsDataBody.put("redirectUrl", redirectUrl);
		
		return new ResultData("S-1", String.format("%d번 글이 생성되었습니다.", newArticleId), rsDataBody);
	}
	
	@RequestMapping("/usr/article/modify")
	public String showModify(Model model, int id, HttpServletRequest request) {
		int loginedMemberId = (int) request.getAttribute("loginedMemberId");

		Map<String, Object> articleModifyAvailableRs = articleService.getArticleModifyAvailable(id, loginedMemberId);

		if (((String) articleModifyAvailableRs.get("resultCode")).startsWith("F-")) {
			model.addAttribute("alertMsg", articleModifyAvailableRs.get("msg"));
			model.addAttribute("historyBack", true);

			return "common/redirect";
		}

		Article article = articleService.getForPrintArticleById(id);

		model.addAttribute("article", article);
		return "article/modify";
	}
	
	@RequestMapping("/usr/article/doModify")
	public String doModify(Model model, @RequestParam Map<String, Object> param, HttpServletRequest request) {
		int loginedMemberId = (int) request.getAttribute("loginedMemberId");

		int id = Integer.parseInt((String) param.get("id"));
		Map<String, Object> articleModifyAvailableRs = articleService.getArticleModifyAvailable(id, loginedMemberId);

		if (((String) articleModifyAvailableRs.get("resultCode")).startsWith("F-")) {
			model.addAttribute("alertMsg", articleModifyAvailableRs.get("msg"));
			model.addAttribute("historyBack", true);

			return "common/redirect";
		}

		Map<String, Object> rs = articleService.modify(param);

		String msg = (String) rs.get("msg");
		String redirectUrl = "/article/detail?id=" + id;
		model.addAttribute("alertMsg", msg);
		model.addAttribute("locationReplace", redirectUrl);
		return "common/redirect";
	}
	
	@RequestMapping("/usr/article/doDelete")
	public String doDelete(Model model, int id, HttpServletRequest request) {

		int loginedMemberId = (int) request.getAttribute("loginedMemberId");

		Map<String, Object> articleDeleteAvailableRs = articleService.getArticleDeleteAvailable(id, loginedMemberId);

		if (((String) articleDeleteAvailableRs.get("resultCode")).startsWith("F-")) {
			model.addAttribute("alertMsg", articleDeleteAvailableRs.get("msg"));
			model.addAttribute("historyBack", true);

			return "common/redirect";
		}

		Map<String, Object> rs = articleService.deleteArticle(id);

		String msg = (String) rs.get("msg");
		String redirectUrl = "/usr/article/list";
		model.addAttribute("alertMsg", msg);
		model.addAttribute("locationReplace", redirectUrl);
		
		return "common/redirect";
	}
}
