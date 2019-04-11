package com.mycompany.emailservice.client;

import com.sendgrid.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailClient {

    private SendGrid sendGrid;

    public EmailClient(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public Response send(Mail email) throws IOException {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(email.build());
        return sendGrid.api(request);
    }

}
