<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Erreur Base de Données</title></head>
<body>
    <h1>Service temporairement indisponible</h1>
    <p>Une erreur de communication avec la base de données est survenue. Veuillez réessayer plus tard.</p>
    <a href="${pageContext.request.contextPath}/produits">Réessayer</a>
</body>
</html>