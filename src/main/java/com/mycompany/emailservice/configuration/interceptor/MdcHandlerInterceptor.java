package com.mycompany.emailservice.configuration.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class MdcHandlerInterceptor extends HandlerInterceptorAdapter {
    private static final StringBuffer QUERY_SEPARATOR = new StringBuffer("?");

    private final RequestContext requestContext;

    public MdcHandlerInterceptor(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put("requestId", requestContext.getRequestId().toString());
        MDC.put("requestUrl", getRequestUrl(request));
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.clear();
    }

    private String getRequestUrl(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        return Optional.of(request)
                .map(HttpServletRequest::getQueryString)
                .map(QUERY_SEPARATOR::append)
                .map(requestURL::append)
                .orElse(requestURL)
                .toString();
    }
}
