package com.exemple.web;

import com.exemple.dao.StockInsuffisantException;
import com.exemple.dao.VenteDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/ventes")
public class VenteServlet extends HttpServlet {

    private VenteDao venteDao;

    @Override
    public void init() throws ServletException {
        venteDao = (VenteDao) getServletContext().getAttribute("venteDao");
        if (venteDao == null) {
            throw new ServletException("VenteDao absent du ServletContext (AppInitListener ?)");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        long produitId;
        int quantite;
        try {
            produitId = Long.parseLong(req.getParameter("produitId"));
            quantite = Integer.parseInt(req.getParameter("quantite"));
            if (quantite <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("flash", "Quantité invalide.");
            resp.sendRedirect(req.getContextPath() + "/produits");
            return;
        }

        try {
            venteDao.vendre(produitId, quantite);
            req.getSession().setAttribute("flash", "Vente enregistrée (" + quantite + " unité(s)).");
        } catch (StockInsuffisantException e) {
            req.getSession().setAttribute("flash", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/produits");
    }
}