<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	 String url = request.getScheme() + "://" + request.getLocalName() + ":" + request.getLocalPort() + request.getContextPath();
	 url = url+"/login";
	 response.sendRedirect(url);
%>
