<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://edia.nl/jsp/tags/edia-sakai-tags" prefix="est"%>
<html>
<head>
<title><spring:message  code="page.update.title"/></title>
<%=request.getAttribute("sakai.html.head")%>
</head>
<body text="#000000" onload="<%= request.getAttribute("sakai.html.body.onload") %>">
	<div class="portletBody">
		<h3><spring:message  code="page.update.header" arguments="${skin.name}"/></h3>
		<c:if test="${not empty rootCauseException}">
			<div class="validation">${rootCauseException}</div>
		</c:if>
		<p class="discTria">
			<spring:message  code="page.update.label.replace"/>
		</p>
    <form action="skinmanager.spring" method="post" enctype="multipart/form-data">
      <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
      <input type="hidden" name="_eventId" id="_eventId" value="submit"/>
      <h4><spring:message  code="page.update.label.upload"/></h4>
      <p class="shorttext">
	      <label for="file"><spring:message  code="page.update.label.file"/></label>
	    	<input class="upload" type="file" name="file" id="file"/>
    	</p>
    	<p class="act">
	      <input type="submit" class="button" value="<spring:message  code="page.update.action.submit"/>"/>
	      <input type="submit" class="button" onclick="document.getElementById('_eventId').value='cancel';" value="<spring:message  code="page.update.action.cancel"/>"/>
      </p>
    </form>	
	</div>
</body>
</html>
