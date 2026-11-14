<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Produits</title>
   <%@ include file="/WEB-INF/views/header.jsp" %>
   </head>
<body class="${bodyClass}">
    <h1>Catalogue de produits</h1>
    
    <form action="${ctx}/produits" method="get" style="margin:10px 0">
	    <input type="text" name="q" placeholder="Rechercher par nom" value="<c:out value='${q}'/>">
	    <button type="submit">Rechercher</button>
	    <a href="${ctx}/produits">Réinitialiser</a>
	</form>

    <c:if test="${not empty flash}">
        <p style="color:green"><c:out value="${flash}"/></p>
    </c:if>

    <a href="${ctx}/produits?action=new">Ajouter un produit</a>

    <c:if test="${empty produits}">S
        <p>Aucun produit</p>
    </c:if>

    <table border="1" cellpadding="6">
        <tr><th>Nom</th><th>Description</th><th>Prix</th><th>Stock</th><th>Actions</th></tr>
        <c:forEach var="p" items="${produits}">
            <tr>
                <td><c:out value="${p.nom}"/></td>
                <td><c:out value="${p.description}"/></td>
                <td><fmt:formatNumber value="${p.prix}" minFractionDigits="2" maxFractionDigits="2"/></td>
                <td style="${p.stock == 0 ? 'color:red' : ''}">${p.stock}</td>
                <td>
                    <a href="${ctx}/produits?action=edit&amp;id=${p.id}">Éditer</a>
                    <form action="${ctx}/produits?action=delete" method="post" style="display:inline"
                          onsubmit="return confirm('Supprimer ce produit ?');">
                        <input type="hidden" name="id" value="${p.id}">
                        <button type="submit">Supprimer</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>

    <a href="${ctx}/DashboardServlet">Retour au tableau de bord</a>
</body>
</html>