package com.sbs.jhs.at.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sbs.jhs.at.dto.Article;
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
	public String showWrite() {
		return "article/write";
	}

	@RequestMapping("/usr/article/doWrite")
	public String doWrite(@RequestParam Map<String, Object> param) {
		int newArticleId = articleService.write(param);

		String redirectUrl = (String) param.get("redirectUrl");
		redirectUrl = redirectUrl.replace("#id", newArticleId + "");

		return "redirect:" + redirectUrl;
	}
}
