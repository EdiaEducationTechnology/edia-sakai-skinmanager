<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://edia.nl/jsp/tags/edia-sakai-tags" prefix="est"%>
<html>
<head>
<title><spring:message code="page.deleteconfirm.title"/></title>
<%=request.getAttribute("sakai.html.head")%>
</head>
<body text="#000000" onload="<%= request.getAttribute("sakai.html.body.onload") %>">
<div class="portletBody">
	<h3><spring:message code="page.deleteconfirm.header" arguments="${skin.name}"/></h3>
		<form action="skinmanager.spring" method="post">
      <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
      <input type="hidden" name="_eventId" id="_eventId" value="no"/>
    	<p class="act">
	      <input type="submit" class="active" value="Yes" onclick="document.getElementById('_eventId').value='yes';"/>
	      <input type="submit" class="button" value="No"/>
      </p>
    </form>	
</div>
</body>
</html>
