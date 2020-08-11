<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="게시물 리스트" />
<%@ include file="../part/head.jspf"%>

<div class="con">
	
		<div class="con">
			<span> <span>총 게시물 수 : </span> <span>${totalCount}</span>
			</span> <span>/</span> <span> <span>현재 페이지 : </span> <span
				style="color: red;">${param.page}</span>
			</span>
		</div>
		
		<div class="con write-box" style="margin-top:30px;">	
			<a class="write" href="./write" style="border:3px solid black;">글쓰기</a>
			<style>a.write:hover{background-color:#efefef;}</style>
		</div>
		
		<div class="con list-box">
			<table border="1">
			<colgroup>
				<col width="82">
				<col width="180">
				<col width="180">
				<col width="80">
				<col width="200">
				<col width="200">
			</colgroup>
			<thead>
				<tr>
					<th>번호</th>
					<th>날짜</th>
					<th>작성자</th>
					<th>조회수</th>
					<th>좋아요</th>
					<th>제목</th>
					<th>비고</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${articles}" var="article">
					<tr class="list-item">
						<td>${article.id}</td>
						<td>${article.regDate}</td>
						<td>임시</td>
						<td>임시</td>
						<td>임시</td><!-- &replyPage=1 -->
						<td class="title"><a href="detail?id=${article.id}" style="display:block;">${article.title}</a></td>
						<style>td.title:hover{background-color:#cfcfdf;}</style>
						<td>임시</td>
					</tr>
					<style>tr.list-item:hover{background-color:#efefef;}</style>
				</c:forEach>
			</tbody>
			</table>
		</div>
	
		<div class="con search-box flex flex-jc-e" style="margin-top:50px;">
	
			<form action="./list">
				<input 	type="text" name="searchKeyword" value="${param.searchKeyword}" />
				<input type="hidden" name="page" value="1" /> 
				<button type="submit">검색</button>
			</form>
	
		</div>
		
		<div class="con page-box" style="margin-top:10px;">
			<ul>
				<c:if test="${page-5 > 0}">
					<span><a href="?searchKeyword=${param.searchKeyword}&page=${page-5}">◀</a></span>
				</c:if>
	
				<c:forEach var="i" begin="${page}" end="${page+4}" step="1">
					<li class="${i == param.page ? 'current' : ''}">
						<a href="?searchKeyword=${param.searchKeyword}&page=${i}" class="block"
							<c:if test="${i > totalPage}">style="display:none;"</c:if>>${i}
						</a>
					</li>
				</c:forEach>
	
				<c:if test="${totalPage-page > 4}">
					<span><a href="?searchKeyword=${param.searchKeyword}&page=${page+5}">▶</a></span>
				</c:if>
			</ul>
		</div>
</div>

<%@ include file="../part/foot.jspf"%>