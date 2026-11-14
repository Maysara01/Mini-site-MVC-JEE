package com.exemple.listener;

import com.exemple.model.ProduitDAO;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitListener implements ServletContextListener {
	 @Override
	 public void contextInitialized(ServletContextEvent sce) {
		 sce.getServletContext().setAttribute("produitDAO", new ProduitDAO());
	 }
}

