//package org.test;
//
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class IframeHeaderFilter extends OncePerRequestFilter {
//
//    private final String option;
//
//    public IframeHeaderFilter(String url) {
//        option = "ALLOW-FROM " + url;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        response.setHeader("X-Frame-Options", option);
//        filterChain.doFilter(request, response);
//    }
//
//}
