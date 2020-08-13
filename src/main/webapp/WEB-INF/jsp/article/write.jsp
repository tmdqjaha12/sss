<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="게시물 작성" />
<%@ include file="../part/head.jspf"%>

<script>
	function ArticleWriteForm__submit(form) {
		
		form.title.value = form.title.value.trim();
		if (form.title.value.length == 0) {
			alert('제목을 입력해주세요.');
			form.title.focus();
			return;
		}
		form.body.value = form.body.value.trim();
		if (form.body.value.length == 0) {
			alert('내용을 입력해주세요.');
			form.body.focus();
			return;
		}

		var maxSizeMb = 50;
		var maxSize = maxSizeMb * 1024 * 1024 //50MB
		
		if (form.file__article__0__common__attachment__1.value) {
			if ( form.file__article__0__common__attachment__1.files[0].size > maxSize ) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요.");
				return;
			} 
		}
		if (form.file__article__0__common__attachment__2.value) {
			if ( form.file__article__0__common__attachment__2.files[0].size > maxSize ) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요.");
				return;
			} 
		}

		var startUploadFiles = function(onSuccess) {
			// 1
			if ( form.file__article__0__common__attachment__1.value.length == 0 && form.file__reply__0__common__attachment__2.value.length == 0 ) {
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

			// 2
		}

		var startWriteArticle = function(fileIdsStr, onSuccess) {
			// 4
			$.ajax({
				url : './../article/doWrite',
				data : {
					fileIdsStr: fileIdsStr,
					title: form.title.value,
					body: form.body.value,
					redirectUrl: form.redirectUrl.value
				},
				dataType:"json",
				type : 'POST',
				success : onSuccess
			});

			// 5
		};

		startUploadFiles(function(data) {
			// 3
			var idsStr = '';
			if ( data && data.body && data.body.fileIdsStr ) {//fileIdsStr
				idsStr = data.body.fileIdsStr;
			}
			
			startWriteArticle(idsStr, function(data) {
				// 7
				if ( data.msg ) {
					alert(data.msg);
				}

				//if ( data.body.redirectUrl ) {
				//	location.replace(data.body.redirectUrl);
				//}
				
				form.body.value = '';
				form.file__article__0__common__attachment__1.value = '';
				form.file__article__0__common__attachment__2.value = '';
				// 8
			});

			// 6
				
		});
	}
</script>
<form class="table-box con form1" onsubmit="ArticleWriteForm__submit(this); return false;">
	<input type="hidden" name="redirectUrl" value="/usr/article/detail?id=#id">
	
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
				<td><input type="submit" value="작성" style="width:100%;"></td>
			</tr>
			
		</tbody>
	</table>
</form>

<%@ include file="../part/foot.jspf"%>