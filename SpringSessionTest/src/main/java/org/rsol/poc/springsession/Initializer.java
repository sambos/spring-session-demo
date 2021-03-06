package org.rsol.poc.springsession;

import javax.servlet.ServletContext;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

public class Initializer extends AbstractHttpSessionApplicationInitializer {

    public Initializer() {
        super(Config.class);
    }
    
    @Override
    protected void afterSessionRepositoryFilter(ServletContext servletContext) {
        appendFilters(servletContext, new UserAccountsFilter());
     }
}