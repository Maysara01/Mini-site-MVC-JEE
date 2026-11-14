# Mini-site MVC JEE : gestion de produits

Mini projet Jakarta EE : application web de gestion de produits (CRUD) construite avec le pattern **MVC**.
Projet réalisé dans le cadre du module JEE (GINF2, ENIAD Berkane), TP 3 : _Du Servlet au MVC_.

## Fonctionnalités

- Connexion / déconnexion avec session
- Tableau de bord réservé aux utilisateurs connectés
- Gestion des produits : liste, ajout, modification, suppression (suppression en POST)
- Validation côté serveur avec erreurs affichées sous chaque champ
- Message flash après chaque opération réussie (pattern Post/Redirect/Get)
- Filtre d'authentification (`AuthFilter`) qui protège le tableau de bord et les produits
- Vues JSP avec EL et JSTL, sans scriptlet, placées sous `WEB-INF/views`
- DAO en mémoire (thread-safe) créé au démarrage par un listener et partagé via le `ServletContext`

**Bonus réalisés :**

- Thème sombre selon le cookie `theme` (`header.jsp` inclus dans toutes les pages)
- Recherche de produits par nom, sans tenir compte de la casse (`/produits?q=lap`)
- Retour à la page demandée après la connexion

## Technologies

- Java, Jakarta EE (Servlets, JSP)
- JSTL 3.0 (jars dans `WEB-INF/lib`)
- Apache Tomcat 10.1
- Eclipse IDE for Enterprise Java (Dynamic Web Project)

## Lancer le projet

1. Importer le dossier dans Eclipse : **File → Import → Existing Projects into Workspace**.
2. Vérifier que les deux jars JSTL sont bien dans `src/main/webapp/WEB-INF/lib`.
3. Ajouter le projet à un serveur **Tomcat 10.1** et le démarrer.
4. Ouvrir : `http://localhost:8082/tpServlet/` (adapter le port et le nom du contexte si besoin).

La page d'accueil redirige automatiquement vers la page de connexion.

**Identifiants de test :** `Maysara` / `251307`

## Structure

```
src/main/java/com/exemple/
├── model/      Produit.java, ProduitDAO.java
├── web/        LoginServlet, LogoutServlet, DashboardServlet, ProduitServlet
├── filter/     AuthFilter.java
└── listener/   AppInitListener.java
src/main/webapp/
├── index.html
└── WEB-INF/
    ├── lib/    jars JSTL
    └── views/  login.jsp, dashboard.jsp, header.jsp, produits/list.jsp, produits/form.jsp
```

## URLs principales

| URL                          | Rôle                         |
| ---------------------------- | ---------------------------- |
| `/LoginServlet`              | Connexion                    |
| `/DashboardServlet`          | Tableau de bord (protégé)    |
| `/produits`                  | Liste des produits (protégé) |
| `/produits?action=new`       | Formulaire d'ajout           |
| `/produits?action=edit&id=1` | Formulaire de modification   |
| `/LogoutServlet`             | Déconnexion                  |

## Réponses aux questions du TP

Voir [REPONSES.md](REPONSES.md).

## Auteur

Maysara, GINF2
