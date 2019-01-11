<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="lang" value="${ITS_USER_SESSION.language}"/>
<input type="hidden" id="lang" value="${lang}" />
<fmt:setLocale value="${lang}" />
