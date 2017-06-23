package org.rsol.poc.springsession.hazlecast;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        //String url = resp.encodeRedirectURL(req.getContextPath() + "/");
       // System.out.println("redirecting to " + req.getContextPath());
        resp.sendRedirect(req.getContextPath());
    }

    private static final long serialVersionUID = 4061762524521437433L;
}