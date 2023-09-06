package org.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CustomInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("Referer in Interceptor : " + request.getHeader("Referer"));
		if(request.getHeader("Referer") == null) {
			System.out.println("Referer is null so redirecting");
			response.sendRedirect("https://localhost:4201/?app=v8");
		}
		
		return true;
	}

}
