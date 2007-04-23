<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://edia.nl/jsp/tags/edia-sakai-tags" prefix="est"%>
<%@page import="java.io.PrintWriter"%>
<html>
<head>
<title><spring:message  code="page.error.title"/></title>
<%=request.getAttribute("sakai.html.head")%>
</head>
<body text="#000000" onload="<%= request.getAttribute("sakai.html.body.onload") %>">
<div class="portletBody">
<h3><spring:message  code="page.error.heading"/></h3>
<c:if test="${not empty rootCauseException}">
	<div class="validation">${rootCauseException.message}</div>
</c:if>
<c:if test="${not empty flowExecutionKey}">
	<div>
		<a href="skinmanager.spring?_flowExecutionKey=${flowExecutionKey}&amp;_eventId=retry">Opnieuw proberen</a>
	</div>
</c:if>
<!-- 
	<% 
	Exception ex1 = (Exception)request.getAttribute("rootCauseException");
	if (ex1 != null) {
		ex1.printStackTrace(new PrintWriter(out));
	}
	%>
 -->
</div>
</body>
</html>
