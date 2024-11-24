package com.robert.codingchallenge.config.filter;

import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class RateLimitingFilter implements Filter {
	private final Bucket bucket;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (bucket.tryConsume(1)) {
			chain.doFilter(request, response);
		} else {
			((HttpServletResponse) response).setStatus(429);
		}
	}
}
