<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://edia.nl/jsp/tags/edia-sakai-tags" prefix="est"%>
<html>
<head>
<title><spring:message  code="page.history.title"/></title>
<%=request.getAttribute("sakai.html.head")%>
</head>
<body text="#000000" onload="<%= request.getAttribute("sakai.html.body.onload") %>">
	<div class="portletBody">
		<h3><spring:message  code="page.history.header" arguments="${skin.name}"/></h3>
		<c:if test="${not empty rootCauseException}">
			<div class="validation">${rootCauseException.message}</div>
		</c:if>
		<p class="discTria">
			<spring:message  code="page.history.label.history"/>
		</p>
		<table class="listHier lines" cellspacing="0">
			<th><spring:message  code="page.history.label.name"/></th>
			<th><spring:message  code="page.history.label.version"/></th>
			<th><spring:message  code="page.history.label.lastModified"/></th>
			<est:allowed permission="skinmanager.edit">
				<th><spring:message  code="page.history.label.action"/></th>
			</est:allowed>
			<c:forEach var="item" items="${history}" varStatus="status">
				<tr>
					<td>${item.name}</td>
					<td>${item.version}</td>
					<td><fmt:formatDate type="both" value="${item.lastModified}"/></td>		
					<c:choose>
						<c:when test="${status.index != 0}">
							<est:allowed permission="skinmanager.edit">
								<td>
									<a href="${flowExecutionUrl}&amp;_eventId=revert&amp;version=${item.version}"><spring:message  code="page.history.action.revert"/></a>	
									|
									<a href="skindownload.spring?id=${item.name}&amp;version=${item.version}"><spring:message  code="page.history.action.download"/></a>
								</td>			
							</est:allowed>
						</c:when>
						<c:otherwise>
							<td>
								<a href="skindownload.spring?id=${item.name}&amp;version=${item.version}"><spring:message  code="page.history.action.download"/></a>	
							</td>
						</c:otherwise>
					</c:choose>
					<td>
					</td>
				</tr>
			</c:forEach>
		</table>
    <form action="skinmanager.spring" method="post">
      <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
      <input type="hidden" name="_eventId" id="_eventId" value="back"/>		
    	<p class="act">
	      <input type="submit" class="active" value="<spring:message  code="page.history.action.back"/>"/>
	    </p>
	  </form>      
	</div>
</body>
</html>
