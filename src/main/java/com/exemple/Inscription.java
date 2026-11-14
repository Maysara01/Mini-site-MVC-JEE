package com.exemple;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet implementation class inscription
 */
@WebServlet("/inscription")
public class Inscription extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Inscription() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8"); 
		response.getWriter().println("<!-- Form HTML -->"); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("UTF-8"); 
		
		 String email = req.getParameter("email"); 
	     String ageStr = req.getParameter("age");
	     String nom = req.getParameter("nom"); 
	         
	        // Validation 
	        if (nom == null || nom.isEmpty()) { 
	            resp.sendRedirect("?erreur=nom_vide"); 
	            return; 
	        } 
	         
	        if (email == null || !email.contains("@")) { 
	            resp.sendRedirect("?erreur=email_invalide"); 
	            return; 
	        } 
	         
	        int age = 0; 
	        try { 
	            age = Integer.parseInt(ageStr); 
	            if (age < 18) { 
	                resp.sendRedirect("?erreur=age_min"); 
	                return; 
	            } 
	        } catch (NumberFormatException e) { 
	            resp.sendRedirect("?erreur=age_invalide"); 
	            return; 
	        } 
	         
	        // Créer l'utilisateur (fictif) 
	        HttpSession session = req.getSession(); 
	        session.setAttribute("utilisateur", nom); 
	         
	        resp.sendRedirect("/tpServlet/SuccesServlet"); 
	}

}
