package org.rsol.poc.springsession.hazlecast;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.HttpSessionManager;

/**
 * Servlet Filter implementation class UserSessionFilter
 */
//@WebFilter("/*")
public class UserSessionFilter implements Filter {

    /**
     * Default constructor. 
     */
    public UserSessionFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
        HttpServletRequest req = (HttpServletRequest) request;

        HttpSessionManager sessionManager =
                (HttpSessionManager) req.getAttribute(HttpSessionManager.class.getName());
        SessionRepository<Session> repo =
                (SessionRepository<Session>) req.getAttribute(SessionRepository.class.getName());

       
        String currentSessionAlias = sessionManager.getCurrentSessionAlias(req);
        Map<String, String> sessionIds = sessionManager.getSessionIds(req);
        String newSessionAlias = String.valueOf(System.currentTimeMillis());
        
        
        String formUrl = sessionManager.encodeURL("./session", currentSessionAlias);
        String logoutUrl = sessionManager.encodeURL("./logout", currentSessionAlias);
        String newSessionUrl = sessionManager.encodeURL("./session", newSessionAlias);
        
        
        req.setAttribute("formUrl", formUrl);
        req.setAttribute("logoutUrl", logoutUrl);
        req.setAttribute("newSessionUrl", newSessionUrl);
        req.setAttribute("sessionIdMap", sessionIds);
        
        // pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
