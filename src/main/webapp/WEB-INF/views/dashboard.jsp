<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Dashboard</title>
    <%@ include file="/WEB-INF/views/header.jsp" %>
</head>
<body class="${bodyClass}">
    <h1>Bienvenue <c:out value="${user}"/></h1>
    <a href="${ctx}/produits"><button type="button">Voir les produits</button></a>
    <a href="${ctx}/LoginServlet"><button type="button">Logout</button></a>
</body>
</html>