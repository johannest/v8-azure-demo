package org.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.spring.server.SpringVaadinServlet;

@Configuration
public class ApplicationConfiguration implements SessionInitListener, SessionDestroyListener {
    private static final Map<String, WrappedSession> sessionStateSessionMap = new HashMap<>();

    @Value("${spring.cloud.azure.active-directory.profile.tenant-id}")
    private String tenantId;
    @Value("${spring.cloud.azure.active-directory.credential.client-id}")
    private String clientId;

    @Autowired
	private CustomInterceptor customInterceptor;
    
    @Bean
    VaadinServlet vaadinServlet() {
        VaadinServlet servlet = new SpringVaadinServlet() {
            @Override
            protected void servletInitialized() throws ServletException {
                super.servletInitialized();
                getService().addSessionInitListener(ApplicationConfiguration.this);
                getService().addSessionDestroyListener(ApplicationConfiguration.this);
            }

            @Override
            protected void service(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
                if (request.getRequestURI().contains("ssologout")) {
                    String sid = request.getParameter("sid");
                    WrappedSession wrappedSession = sessionStateSessionMap.get(sid);
                    if (wrappedSession != null) {
                        wrappedSession.invalidate();
                    }
                    response.sendRedirect("https://login.microsoftonline.com/"+tenantId+"/oauth2/v2.0/logout?client_id="+clientId);
                } else {
                    super.service(request, response);
                }
            }
        };

        return servlet;
    }

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        String sid = (String) event.getSession().getSession().getAttribute("session_state");
        System.out.println("%%% Session init: " + event.getSession().getSession().getId()+" sid: "+sid);
        sessionStateSessionMap.put(sid, event.getSession().getSession());
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent event) {
        System.out.println("%%% Session destroyed: " + ((event.getSession() != null && event.getSession().getSession() != null) ? event.getSession().getSession().getId() : "null"));
    }

	@Bean
	public MappedInterceptor myInterceptor() {
		String excludedPatterns[] = { "/ssologout" };
		MappedInterceptor intercepror = new MappedInterceptor(null, excludedPatterns, customInterceptor);
		return intercepror;
	}
}
