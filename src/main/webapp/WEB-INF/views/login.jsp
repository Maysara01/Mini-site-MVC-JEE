<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <%@ include file="/WEB-INF/views/header.jsp" %>
</head>
<body class="${bodyClass}">

    <h1>Connexion</h1>

    <c:if test="${not empty erreur}">
        <p style="color:red"><c:out value="${erreur}"/></p>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/LoginServlet">
        <input type="text" name="username" placeholder="Username"
               value="<c:out value='${username}'/>" required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit">Se connecter</button>
    </form>
</body>
</html>