package org.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

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
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/VAADIN/**").permitAll()
                .anyRequest().authenticated()
                .and().oauth2Login()
                .and().logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .logoutSuccessHandler(logoutHandler)
                .permitAll();

//                http.authorizeRequests().anyRequest()
//                .permitAll();

        return http.build();
    }

//    @Bean
//    public IframeHeaderFilter createFilter() {
//        return new IframeHeaderFilter(appShellURL);
//    }

}
