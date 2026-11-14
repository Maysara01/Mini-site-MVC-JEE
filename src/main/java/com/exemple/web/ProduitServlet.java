package com.exemple.web;

import com.exemple.model.Produit;
import com.exemple.model.ProduitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@WebServlet("/produits")
public class ProduitServlet extends HttpServlet {

    private static final String LIST_VIEW = "/WEB-INF/views/produits/list.jsp";
    private static final String FORM_VIEW = "/WEB-INF/views/produits/form.jsp";

    private ProduitDAO dao;

    @Override
    public void init() throws ServletException {
        dao = (ProduitDAO) getServletContext().getAttribute("produitDAO");
        if (dao == null) {
            throw new ServletException("ProduitDAO absent du ServletContext (AppInitListener ?)");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            HttpSession session = req.getSession(false);
            if (session != null && session.getAttribute("flash") != null) {
                req.setAttribute("flash", session.getAttribute("flash"));
                session.removeAttribute("flash");
            }

            String q = trim(req.getParameter("q"));
            List<Produit> liste = dao.findAll();
            if (!q.isEmpty()) {
                String needle = q.toLowerCase(Locale.ROOT);
                liste = liste.stream()
                        .filter(p -> p.getNom() != null
                                  && p.getNom().toLowerCase(Locale.ROOT).contains(needle))
                        .collect(Collectors.toList());
            }
            req.setAttribute("produits", liste);
            req.setAttribute("q", q);

            req.getRequestDispatcher(LIST_VIEW).forward(req, resp);

        } else if ("new".equals(action)) {
            req.setAttribute("produit", new Produit());
            req.getRequestDispatcher(FORM_VIEW).forward(req, resp);

        } else if ("edit".equals(action)) {
            Long id = parseId(req.getParameter("id"));
            Produit p = dao.findById(id);
            if (p == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            req.setAttribute("produit", p);
            req.getRequestDispatcher(FORM_VIEW).forward(req, resp);

        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // ---- DELETE ----
        if ("delete".equals(req.getParameter("action"))) {
            Produit p = dao.findById(parseId(req.getParameter("id")));
            if (p == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            dao.delete(p.getId());
            req.getSession().setAttribute("flash", "Produit « " + p.getNom() + " » supprimé.");
            resp.sendRedirect(req.getContextPath() + "/produits");
            return;
        }

        // ---- CREATE / UPDATE ----
        String idParam = req.getParameter("id");
        Long id = null;
        if (idParam != null && !idParam.isBlank()) {
            id = parseId(idParam);
            if (dao.findById(id) == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }

        String nom = trim(req.getParameter("nom"));
        String description = trim(req.getParameter("description"));
        String prixStr = trim(req.getParameter("prix"));
        String stockStr = trim(req.getParameter("stock"));

        Map<String, String> erreurs = new HashMap<>();

        if (nom.isEmpty()) {
            erreurs.put("nom", "Le nom est obligatoire.");
        } else if (nom.length() < 2 || nom.length() > 50) {
            erreurs.put("nom", "Le nom doit contenir entre 2 et 50 caractères.");
        }

        BigDecimal prix = null;
        if (prixStr.isEmpty()) {
            erreurs.put("prix", "Le prix est obligatoire.");
        } else {
            try {
                prix = new BigDecimal(prixStr.replace(',', '.'));
                if (prix.signum() <= 0) {
                    erreurs.put("prix", "Le prix doit être strictement positif.");
                }
            } catch (NumberFormatException e) {
                erreurs.put("prix", "Le prix doit être un nombre décimal.");
            }
        }

        int stock = 0;
        if (!stockStr.isEmpty()) {
            try {
                stock = Integer.parseInt(stockStr);
                if (stock < 0) {
                    erreurs.put("stock", "Le stock doit être supérieur ou égal à 0.");
                }
            } catch (NumberFormatException e) {
                erreurs.put("stock", "Le stock doit être un entier.");
            }
        }

        if (description.length() > 255) {
            erreurs.put("description", "La description ne doit pas dépasser 255 caractères.");
        }

        Produit produit = new Produit(id, nom, description, prix, stock);

        if (!erreurs.isEmpty()) {
            req.setAttribute("erreurs", erreurs);
            req.setAttribute("produit", produit);
            req.getRequestDispatcher(FORM_VIEW).forward(req, resp);
            return;
        }

        dao.save(produit);
        req.getSession().setAttribute("flash", "Produit « " + nom + " » enregistré.");
        resp.sendRedirect(req.getContextPath() + "/produits");
    }

    // ---- helpers ----
    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }


    private static Long parseId(String s) {
        try {
            return Long.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}