package org.rsol.poc.springsession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;

@Configuration
public class Config {

    @Bean
    public <S extends ExpiringSession> SessionRepositoryFilter<? extends ExpiringSession> springSessionRepositoryFilter(SessionRepository<S> sessionRepository) {
        SessionRepositoryFilter<S> sessionRepositoryFilter = new SessionRepositoryFilter<S>(sessionRepository);
/*        if(httpSessionStrategy != null) {
            sessionRepositoryFilter.setHttpSessionStrategy(httpSessionStrategy);
        }*/
        return sessionRepositoryFilter;
    }
    
    @Bean
    public MapSessionRepository sessionRepository() {
        MapSessionRepository sessionRepository = new MapSessionRepository();
        return sessionRepository;
    }

    
}
