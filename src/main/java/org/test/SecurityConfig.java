package org.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@ConditionalOnProperty(name = "spring.cloud.azure.active-directory.enabled", havingValue = "true")
public class SecurityConfig {

    @Value("${app.shell.url:http://localhost:4201}")
    private String appShellURL;

    private SimpleUrlLogoutSuccessHandler logoutHandler = new SimpleUrlLogoutSuccessHandler();

    public SecurityConfig() {
        logoutHandler.setDefaultTargetUrl("https://login.microsoftonline.com/8929c98e-3ed8-49c3-baff-5cf4f79214d1/oauth2/v2.0/logout?client_id=1bb7f6f3-360e-4430-bb7e-775821b66827");
    }

    @Bean
    SecurityFilterChain aadFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.headers().contentSecurityPolicy("frame-ancestors 'self' "+appShellURL);
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/VAADIN/**").permitAll()
                .antMatchers("/ssologout").permitAll()
                .antMatchers("/ssologout/**").permitAll()
                .antMatchers("/logout").permitAll()
                .anyRequest().authenticated()
                .and().oauth2Login()
                .successHandler(new SimpleUrlAuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        // azure session_state needed for single sign out (=sid param in sign out request)
                        String sid = request.getParameter("session_state");
                        HttpSession session = request.getSession();
                        System.out.println(session.getId()+" -- "+sid);
                        session.setAttribute("session_state", sid);
                        super.onAuthenticationSuccess(request, response, authentication);
                    }
                })
                .and().logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .logoutSuccessHandler(logoutHandler)
                .permitAll();

        return http.build();
    }

}
