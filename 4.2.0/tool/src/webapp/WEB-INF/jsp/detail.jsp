<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://edia.nl/jsp/tags/edia-sakai-tags" prefix="est"%>
<html>
<head>
<title><spring:message  code="page.detail.title"/></title>
<%=request.getAttribute("sakai.html.head")%>
</head>
<body text="#000000" onload="<%= request.getAttribute("sakai.html.body.onload") %>">
	<div class="portletBody">
		<div class="navIntraTool">
			<est:allowed permission="skinmanager.edit">
				<a href="skinmanager.spring?_flowExecutionKey=${flowExecutionKey}&amp;_eventId=update"><spring:message  code="page.detail.action.update"/></a>
			</est:allowed>
			<est:allowed permission="skinmanager.delete">
				<a href="skinmanager.spring?_flowExecutionKey=${flowExecutionKey}&amp;_eventId=delete"><spring:message  code="page.detail.action.delete"/></a>
			</est:allowed>
			<a href="skinmanager.spring?_flowExecutionKey=${flowExecutionKey}&amp;_eventId=history"><spring:message  code="page.detail.action.history"/></a>
		</div>
		<h3><spring:message  code="page.detail.header" arguments="${skin.name}"/></h3>
		<c:if test="${not empty rootCauseException}">
			<div class="validation">${rootCauseException.message}</div>
		</c:if>
		<p class="discTria">
			<spring:message  code="page.detail.label.files"/>
		</p>
		<table class="listHier lines" cellspacing="0">
			<tr>
				<th><spring:message  code="page.detail.label.name"/></th>
				<th><spring:message  code="page.detail.label.size"/></th>
				<th><spring:message  code="page.detail.label.lastModified"/></th>
			</tr>
			<c:forEach var="file" items="${skin.files}">
				<tr>
					<td>${file.name}</td>
					<td>${file.size}</td>
					<td><fmt:formatDate type="both" value="${file.lastModified}"/></td>			
				</tr>
			</c:forEach>
		</table>
		<c:if test="${not empty sites}">
			<h3>
				<spring:message  code="page.detail.label.sites"/>
			</h3>
			<table class="listHier lines" cellspacing="0">
				<th>Titel</th>
				<c:forEach var="site" items="${sites}">
					<tr>
						<td>${site}</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>		
		
		<form action="skinmanager.spring" method="post">
	      	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
	      	<input type="hidden" name="_eventId" id="_eventId" value="back"/>
    		<p class="act">
	      		<input type="submit" class="active" value="<spring:message  code="page.detail.action.back"/>"/>
      		</p>
    </form>			
	</div>
</body>
</html>
