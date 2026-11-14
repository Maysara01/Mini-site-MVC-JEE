<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Produit</title> 
  <%@ include file="/WEB-INF/views/header.jsp" %></head>
<body class="${bodyClass}">
    <h1>${empty produit.id ? 'Nouveau produit' : 'Modifier le produit'}</h1>

    <form action="${ctx}/produits" method="post">
        <c:if test="${not empty produit.id}">
            <input type="hidden" name="id" value="${produit.id}">
        </c:if>

        <p>
            <label>Nom :</label><br>
            <input type="text" name="nom" value="<c:out value='${produit.nom}'/>">
            <c:if test="${not empty erreurs.nom}">
                <br><span style="color:red"><c:out value="${erreurs.nom}"/></span>
            </c:if>
        </p>

        <p>
            <label>Description :</label><br>
            <textarea name="description" rows="3" cols="40"><c:out value="${produit.description}"/></textarea>
            <c:if test="${not empty erreurs.description}">
                <br><span style="color:red"><c:out value="${erreurs.description}"/></span>
            </c:if>
        </p>

        <p>
            <label>Prix :</label><br>
            <input type="text" name="prix"
                   value="<c:out value='${not empty param.prix ? param.prix : produit.prix}'/>">
            <c:if test="${not empty erreurs.prix}">
                <br><span style="color:red"><c:out value="${erreurs.prix}"/></span>
            </c:if>
        </p>

        <p>
            <label>Stock :</label><br>
            <input type="text" name="stock"
                   value="<c:out value='${not empty param.stock ? param.stock : produit.stock}'/>">
            <c:if test="${not empty erreurs.stock}">
                <br><span style="color:red"><c:out value="${erreurs.stock}"/></span>
            </c:if>
        </p>

        <button type="submit">Enregistrer</button>
        <a href="${ctx}/produits">Annuler</a>
    </form>
</body>
</html>