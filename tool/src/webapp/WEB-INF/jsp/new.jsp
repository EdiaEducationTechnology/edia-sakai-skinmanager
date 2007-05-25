<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://edia.nl/jsp/tags/edia-sakai-tags" prefix="est"%>
<%@page import="java.io.PrintWriter"%>
<%@page contentType="text/html; charset=UTF-8"%>
<html>
<head>
	<title><spring:message  code="page.new.title"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%=request.getAttribute("sakai.html.head")%>
</head>
<body text="#000000" onload="<%= request.getAttribute("sakai.html.body.onload") %>">
	<div class="portletBody">
		<h3><spring:message  code="page.new.header"/></h3>	
		<c:if test="${not empty rootCauseException}">
			<div class="validation">${rootCauseException.message}</div>
		</c:if>
		<p class="discTria">
			<spring:message  code="page.new.label.upload"/>
		</p>
    	<form action="skinmanager.spring" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
      		<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
      		<input type="hidden" name="_eventId" id="_eventId" value="submit"/>
	  		<h4><spring:message  code="page.new.label.upload.file"/></h4>
			<p class="shorttext">
	      		<label for="id"><spring:message  code="page.new.label.name"/></label>
				<input type="text" name="id" id="id" value="${id}"/>
			</p>
			<p class="shorttext">
				<label for="file"><spring:message  code="page.new.label.file"/></label>
	    		<input type="file" name="file" id="file" /><br/>
    		</p>
   			<p class="act">
      			<input type="submit" class="active" value="<spring:message  code="page.new.action.submit"/>"/>
	      		<input type="submit" class="button" onclick="document.getElementById('_eventId').value='cancel';" value="<spring:message  code="page.new.action.cancel"/>"/>
      		</p>
    	</form>	
	</div>
	<!-- 
	<% 
	Exception ex1 = (Exception)request.getAttribute("rootCauseException");
	if (ex1 != null) {
		ex1.printStackTrace(new PrintWriter(out));
	}
	%>
	 -->
</body>
</html>
