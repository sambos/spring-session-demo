package org.rsol.poc.springsession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.HttpSessionManager;

public class UserAccountsFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        HttpSessionManager sessionManager =
                (HttpSessionManager) req.getAttribute(HttpSessionManager.class.getName());
        SessionRepository<Session> repo =
                (SessionRepository<Session>) req.getAttribute(SessionRepository.class.getName());

        String currentSessionAlias = sessionManager.getCurrentSessionAlias(req);
        Map<String, String> sessionIds = sessionManager.getSessionIds(req);
        String newSessionAlias = String.valueOf(System.currentTimeMillis());

        String contextPath = req.getContextPath();
        List<Account> accounts = new ArrayList<Account>();
        Account currentAccount = null;
        for(Map.Entry<String, String> entry : sessionIds.entrySet()) {
            String alias = entry.getKey();
            String sessionId = entry.getValue();

            Session session = repo.getSession(sessionId);
            if(session == null) {
                continue;
            }

            String username = (String)session.getAttribute("username");
            if(username == null) {
                newSessionAlias = alias;
                continue;
            }

            String logoutUrl = sessionManager.encodeURL("./logout", alias);
            String switchAccountUrl = sessionManager.encodeURL("./", alias);
            Account account = new Account(username, logoutUrl, switchAccountUrl);
            if(currentSessionAlias.equals(alias)) {
                currentAccount = account;
            } else {
                accounts.add(account);
            }
        }

        req.setAttribute("currentAccount", currentAccount);
        req.setAttribute("addAccountUrl", sessionManager.encodeURL(contextPath, newSessionAlias));
        req.setAttribute("accounts", accounts);

        chain.doFilter(request, response);
    }

    public void destroy() {
    }

}