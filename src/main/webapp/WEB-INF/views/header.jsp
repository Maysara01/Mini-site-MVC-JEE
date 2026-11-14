<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="bodyClass" value="${cookie.theme.value == 'dark' ? 'dark' : ''}"/>
<style>
    body.dark { background:#1e1e1e; color:#eee; }
    body.dark a { color:#8ab4f8; }
    body.dark table, body.dark th, body.dark td { border-color:#666; }
</style>