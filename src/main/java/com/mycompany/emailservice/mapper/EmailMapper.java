package com.mycompany.emailservice.mapper;

import com.mycompany.emailservice.model.EmailDto;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Personalization;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {

    public Mail toEmail(EmailDto emailDto) {

        Email from = new Email(emailDto.getSender());

        Personalization personalization = new Personalization();
        emailDto.getRecipients().stream().map(Email::new).forEach(personalization::addTo);
        if (emailDto.getCarbonCopyRecipients() != null) {
            emailDto.getCarbonCopyRecipients().stream().map(Email::new).forEach(personalization::addCc);
        }
        if (emailDto.getBlindCarbonCopyRecipients() != null) {
            emailDto.getBlindCarbonCopyRecipients().stream().map(Email::new).forEach(personalization::addBcc);
        }

        String subject = emailDto.getSubject();
        Content content = new Content("text/plain", emailDto.getBody());

        Mail mail = new Mail();
        mail.addPersonalization(personalization);

        mail.setFrom(from);
        mail.setSubject(subject);
        mail.addContent(content);

        return mail;
    }
}
