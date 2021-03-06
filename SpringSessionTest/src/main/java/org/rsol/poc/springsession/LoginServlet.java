package org.rsol.poc.springsession;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if(username != null && !"".equals(username) && username.equals(password)) {
            req.getSession().setAttribute("username", username);
            String url = resp.encodeRedirectURL(req.getContextPath() + "/");
            resp.sendRedirect(url);
        } else {
            String url = resp.encodeRedirectURL(req.getContextPath() + "/?error");
            resp.sendRedirect(url);
        }
    }

    private static final long serialVersionUID = -8157634860354132501L;
}
