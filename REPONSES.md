# TP 3 : Du Servlet au MVC - Réponses

**Étudiant :** Maysara - GINF2
**Module :** JEE - Pr. Hayat OUADI

---

## Question 1 : Pourquoi un `forward` quand le login échoue, mais un `sendRedirect` quand il réussit ?

**Échec : forward.** Le forward reste dans la même requête. L'attribut `erreur` et le login saisi (`username`) sont placés dans la requête, et `login.jsp` peut donc les afficher. L'URL ne change pas et l'utilisateur garde ce qu'il a tapé.

**Succès : redirect.** Le redirect demande au navigateur d'envoyer une **nouvelle requête** (GET) vers le tableau de bord. L'URL change, et si l'utilisateur appuie sur F5, le navigateur ne renvoie pas le formulaire de connexion (POST), il recharge seulement le tableau de bord.

---

## Question 2 : Que se passe-t-il avec F5 juste après avoir ajouté un produit ?

**(a) Si `doPost` fait un forward vers la liste :** l'URL du navigateur reste celle du POST. F5 renvoie donc le même POST et le produit est ajouté une **deuxième fois** (le navigateur demande aussi une confirmation de renvoi du formulaire).

**(b) Si `doPost` fait un redirect :** le navigateur fait ensuite un GET sur `/produits`. F5 ne refait que ce GET : **aucun doublon**.

Ce pattern s'appelle **PRG : Post / Redirect / Get**. Il est appliqué dans `ProduitServlet` après chaque POST réussi (création, modification, suppression), avec un message flash stocké en session.

---

## Question 3 : Nom de produit = `<script>alert('XSS')</script>`

- Avec `${produit.nom}` : le texte est écrit tel quel dans le HTML. Le navigateur l'interprète comme une balise `<script>` et **exécute le code** (une fenêtre `XSS` s'affiche). C'est une faille **XSS** (Cross-Site Scripting).
- Avec `<c:out value="${produit.nom}"/>` : les caractères spéciaux sont échappés (`&lt;script&gt;...`). Le texte s'affiche tel quel, comme du texte, et **rien ne s'exécute**.

**Conclusion :** toute donnée saisie par l'utilisateur doit être échappée avant d'être affichée. Dans les JSP, on utilise `<c:out>` ou `${fn:escapeXml(...)}`.

---

## Question 4 : Avantage du filtre par rapport à la vérification dans chaque servlet. Deux autres usages classiques.

**Avantages du filtre `AuthFilter` :**

- Le contrôle d'accès est écrit **une seule fois** au lieu d'être recopié dans chaque servlet.
- On ne peut pas **oublier** de protéger une nouvelle page : il suffit d'ajouter son URL au filtre.
- Un changement de règle (par exemple rediriger ailleurs) se fait à **un seul endroit**.
- Les servlets deviennent plus simples : elles ne contiennent plus le test `if (user == null)`.

**Autres usages classiques des filtres :**

1. **Journalisation (logs)** des requêtes : URL, durée, utilisateur.
2. **Encodage des caractères** (par exemple forcer UTF-8 sur toutes les requêtes).
