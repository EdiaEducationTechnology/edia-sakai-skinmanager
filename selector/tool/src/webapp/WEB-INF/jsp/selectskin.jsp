<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://edia.nl/jsp/tags/edia-sakai-tags" prefix="est"%>
<html>
<head>
<title><spring:message code="page.index.title" /></title>
<%=request.getAttribute("sakai.html.head")%>
<c:set var="pagename" value="selectskin" />
</head>
<body text="#000000" onload="<%= request.getAttribute("sakai.html.body.onload") %>">
<div class="portletBody">
	<h3><s:message code="page.${pagename}.header" /></h3>
	<p class="discTria">
		<s:message  code="page.${pagename}.label.selectskin" htmlEscape="true"/>
	</p>
	<form action="index.spring" method="post">
	<c:if test="${!hasPermission}">
		<div class="validation"><s:message  code="page.${pagename}.validation.nopermission" htmlEscape="true"/></div>
	</c:if>
	<c:if test="${command.updated}">
		<span class="information"><s:message  code="page.${pagename}.information.updated" htmlEscape="true"/></span>
	</c:if>
	<p class="shorttext">
		<s:bind path="command.skin">
			<label for="${status.expression}"> <strong><s:message code="label.${status.expression}" /></strong> </label>
			<select name="${status.expression}">
				<c:forEach var="skin" items="${skins}">
					<option value="${skin}" ${skin == status.value ? "selected=\"selected\"" : ""}">${skin}</option>
				</c:forEach>
			</select>
		</s:bind>
	</p>
	
	<p class="act">
		<input type="submit" accesskey="c" value="<s:message code="action.save"/>" ${hasPermission ? "" : "disabled=\"disabled\""}></input>
	</p>
	</form>
</div>
</body>
</html>