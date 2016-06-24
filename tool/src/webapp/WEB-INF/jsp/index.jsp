<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://edia.nl/jsp/tags/edia-sakai-tags" prefix="est"%>
<html>
<head>
<title><spring:message  code="page.index.title"/></title>
<%=request.getAttribute("sakai.html.head")%>
</head>
<body text="#000000" onload="<%= request.getAttribute("sakai.html.body.onload") %>">
<div class="portletBody">
	<est:allowed permission="skinmanager.create">
		<div class="navIntraTool">
			<a href="skinmanager.spring?_flowExecutionKey=${flowExecutionKey}&amp;_eventId=new"><spring:message  code="page.index.action.new"/></a>
		</div>
	</est:allowed>

<h3><spring:message  code="page.index.header"/></h3>

<est:allowed permission="skinmanager.view">
	<div class="instruction"><spring:message  code="page.index.label.installed"/></div>
	<table class="listHier lines" cellspacing="0">
		<tr>
			<th><spring:message  code="page.index.label.name"/></th>
			<th><spring:message  code="page.index.label.size"/></th>
			<th><spring:message  code="page.index.label.lastModified"/></th>
		</tr>
		<c:forEach var="skin" items="${skins}">
			<tr>
				<td>
					<est:allowed permission="skinmanager.view">
						<a href="${flowExecutionUrl}&amp;_eventId=edit&amp;id=${skin.name}">
					</est:allowed> 
						${skin.name} 
					<est:allowed permission="skinmanager.edit">
						</a>
					</est:allowed>
				</td>
				<td>${skin.size}</td>
				<td><fmt:formatDate type="both" value="${skin.lastModified}"/></td>
			</tr>
		</c:forEach>
	</table>
</est:allowed>
</div>
</body>
</html>
