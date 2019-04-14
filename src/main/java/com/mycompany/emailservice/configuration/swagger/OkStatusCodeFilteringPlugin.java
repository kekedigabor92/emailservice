package com.mycompany.emailservice.configuration.swagger;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

@Component
public class OkStatusCodeFilteringPlugin implements OperationBuilderPlugin {

    @Override
    public void apply(OperationContext operationContext) {
        if (!operationContext.httpMethod().equals(HttpMethod.GET)) {
            operationContext
                    .operationBuilder()
                    .build()
                    .getResponseMessages()
                    .removeIf(responseMessage -> responseMessage.getCode() == HttpStatus.OK.value());
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
