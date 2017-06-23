package org.rsol.poc.springsession.hazlecast;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/session")
public class SessionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String attributeName = req.getParameter("attributeName");
        String attributeValue = req.getParameter("attributeValue");
        req.getSession().setAttribute(attributeName, attributeValue);
        
       
        //String url = resp.encodeRedirectURL(req.getContextPath() + "/");
        //resp.sendRedirect(url);
        //String url =  resp.encodeURL(req.getContextPath() + "/");
        
        req.getRequestDispatcher("/").forward(req, resp);
        //display all req parameters
        //display all session parameters
    }
    

    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doPost(req,resp);
    }
    
    private static final long serialVersionUID = 2878267318695777395L;
}