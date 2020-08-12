<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="게시물 작성" />
<%@ include file="../part/head.jspf"%>

<script>
	function ArticleWriteForm__submit(form) {
		form.title.value = form.title.value.trim();

		if (form.title.value.length == 0) {
			form.title.focus();
			alert('제목을 입력해주세요.');

			return;
		}

		form.body.value = form.body.value.trim();

		if (form.body.value.length == 0) {
			form.body.focus();
			alert('내용을 입력해주세요.');

			return;
		}

		var startUploadFiles = function(onSuccess) {
			if ( form.file__article__0__common__attachment__1.value.length == 0 && form.file__article__0__common__attachment__2.value.length == 0 ) {
				onSuccess();
				return;
			}

			var fileUploadFormData = new FormData(form); 
			
			fileUploadFormData.delete("relTypeCode");
			fileUploadFormData.delete("relId");
			fileUploadFormData.delete("body");

			$.ajax({
				url : './../file/doUploadAjax',
				data : fileUploadFormData,
				processData : false,
				contentType : false,
				dataType:"json",
				type : 'POST',
				success : onSuccess
			});
		}

		var startWriteArticle = function(fileIdsStr, onSuccess) {
			$.ajax({
				url : './../article/doWrite',
				data : {
					fileIdsStr: fileIdsStr,
					relTypeCode: form.relTypeCode.value,
					relId: form.relId.value,
					body: form.body.value,
					title: form.title.value
					//,
					//redirectUrl: form.redirectUrl.value
				},
				dataType:"json",
				type : 'POST',
				success : onSuccess
			});
		};

		startUploadFiles(function(data) {
			var idsStr = '';
			if ( data && data.body && data.body.fileIdsStr ) {
				idsStr = data.body.fileIdsStr;
			}
			startWriteArticle(idsStr, function(data) {
				if ( data.msg ) {
					alert(data.msg);
				}
				
				form.body.value = '';
				form.file__article__0__common__attachment__1.value = '';
				form.file__article__0__common__attachment__2.value = '';

				//var locationReplaceUrl = data.body.get("redirectUrl").trim;
				//var locationReplaceUrl = data.body.redirectUrl.trim();
				//if (locationReplaceUrl) {
				//	location.replace(locationReplaceUrl);
				//}
			});
		});
	}
</script>
<form class="table-box con form1"
	onsubmit="ArticleWriteForm__submit(this); return false;">
	<input type="hidden" name="redirectUrl" value="/usr/article/detail?id=#id">
	<input type="hidden" name="relId" value="${articleMaxId}" />

	<table>
		<tbody>
			<tr>
				<th>제목</th>
				<td>
					<div class="form-control-box">
						<input type="text" placeholder="제목을 입력해주세요." name="title"
							maxlength="100" />
					</div>
				</td>
			</tr>
			<tr>
				<th>내용</th>
				<td>
					<div class="form-control-box">
						<textarea placeholder="내용을 입력해주세요." name="body" maxlength="2000"></textarea>
					</div>
				</td>
			</tr>
			
			<tr>
				<th>첨부1 비디오</th>
				<td>
					<div class="form-control-box">
						<input type="file" accept="video/*" capture
							name="file__article__0__common__attachment__1">
					</div>
				</td>
			</tr>
			<tr>
				<th>첨부2 비디오</th>
				<td>
					<div class="form-control-box">
						<input type="file" accept="video/*" capture
							name="file__article__0__common__attachment__2">
					</div>
				</td>
			</tr>
			
			<tr>
				<th>작성</th>
				<td>
					<button class="btn btn-primary" type="submit" style="width:100%;">작성</button>
				</td>
			</tr>
		</tbody>
	</table>
</form>

<%@ include file="../part/foot.jspf"%>