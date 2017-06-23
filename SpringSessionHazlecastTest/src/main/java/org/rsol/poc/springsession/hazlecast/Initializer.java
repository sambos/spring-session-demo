package org.rsol.poc.springsession.hazlecast;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.EnumSet;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

@WebListener
public class Initializer implements ServletContextListener {
    private HazelcastInstance instance;

    public void contextInitialized(ServletContextEvent sce) {
        String sessionMapName = "spring:session:sessions";
        ServletContext sc = sce.getServletContext();

        Config cfg = new Config();
        NetworkConfig netConfig = new NetworkConfig();
        netConfig.setPort(getAvailablePort());
        cfg.setNetworkConfig(netConfig);
        SerializerConfig serializer = new SerializerConfig()
            .setTypeClass(Object.class)
            .setImplementation(new ObjectStreamSerializer());
        cfg.getSerializationConfig().addSerializerConfig(serializer);
        MapConfig mc = new MapConfig();
        mc.setName(sessionMapName);
        mc.setTimeToLiveSeconds(MapSession.DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS);
        cfg.addMapConfig(mc);

        instance = Hazelcast.newHazelcastInstance(cfg);
        Map<String,ExpiringSession> sessions = instance.getMap(sessionMapName);
        
        //Map<String,ExpiringSession> sessions = new HashMap<String,ExpiringSession>();
        SessionRepository<ExpiringSession> sessionRepository = new MapSessionRepository(sessions);
        Dynamic fr = sc.addFilter("springSessionFilter", new SessionRepositoryFilter<ExpiringSession>(sessionRepository ));
        fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.ASYNC,DispatcherType.FORWARD );
        fr = sc.addFilter("userSessionFilter", new UserSessionFilter());
        fr.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
        //.addFilter("UserSessionFilter", new UserSessionFilter());
        
    }

    public void contextDestroyed(ServletContextEvent sce) {
        instance.shutdown();
    }

    private static int getAvailablePort() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
            return socket.getLocalPort();
        } catch(IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            }catch(IOException e) {}
        }
    }
}