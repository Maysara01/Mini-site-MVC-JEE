package com.exemple.listener;

import com.exemple.dao.ProduitDao;
import com.exemple.dao.ProduitDaoJdbc;
import com.exemple.dao.VenteDao;
import com.exemple.dao.VenteDaoJdbc;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ProduitDao produitDao = new ProduitDaoJdbc();
        VenteDao venteDao = new VenteDaoJdbc();
        sce.getServletContext().setAttribute("produitDao", produitDao);
        sce.getServletContext().setAttribute("venteDao", venteDao);
    }
}