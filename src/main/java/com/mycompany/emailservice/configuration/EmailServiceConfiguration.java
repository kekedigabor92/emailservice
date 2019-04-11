package com.mycompany.emailservice.configuration;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailServiceConfiguration {

    @Bean
    public SendGrid sendGrid(@Value("${email-service-provider.apikey}") String sendGridApiKey) {
        return new SendGrid(sendGridApiKey);
    }
}
