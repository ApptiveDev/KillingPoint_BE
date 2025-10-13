package apptive.team5.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String loggingId = UUID.randomUUID().toString();

        try {
            long startTime = System.currentTimeMillis();
            log.trace("[REQUEST] id:{}, request URI: {}, METHOD:{}", loggingId, request.getRequestURI(), request.getMethod());
            filterChain.doFilter(request, response);
            long endTime = System.currentTimeMillis();
            log.trace("[RESPONSE] id:{},  HTTP STATUS:{}, TIME:{}ms ", loggingId, response.getStatus(), endTime - startTime);
        } catch (Exception e) {
            log.error("[REQUEST] id:{}, request URI: {}, METHOD:{}", loggingId, request.getRequestURI(), request.getMethod());
            log.error("[RESPONSE] id:{}, ERROR:{}", loggingId, e.getMessage(), e);
            throw e;
        }
    }
}
