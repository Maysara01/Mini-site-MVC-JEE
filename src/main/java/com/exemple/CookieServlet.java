package com.exemple;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class CookieServlet
 */
@WebServlet("/cookies")
public class CookieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CookieServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String action = req.getParameter("action"); 
        
        if ("set".equals(action)) { 
            Cookie cookie = new Cookie("theme", "dark"); 
            cookie.setMaxAge(30 * 24 * 60 * 60);  // 30 jours 
            cookie.setPath("/"); 
            resp.addCookie(cookie); 
        } 
         
        // Afficher tous les cookies 
        resp.setContentType("text/html"); 
        Cookie[] cookies = req.getCookies(); 
        if (cookies != null) { 
            for (Cookie c : cookies) { 
                resp.getWriter().println(c.getName() + ": " + c.getValue()); 
            } 
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
