package com.mycompany.emailservice.configuration.interceptor;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestContext {

    private final UUID requestId;

    public RequestContext() {
        requestId = UUID.randomUUID();
    }

    public UUID getRequestId() {
        return requestId;
    }
}