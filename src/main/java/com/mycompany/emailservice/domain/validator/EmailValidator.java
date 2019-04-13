package com.mycompany.emailservice.domain.validator;

import com.mycompany.emailservice.domain.model.EmailDto;
import com.mycompany.emailservice.domain.model.ErrorDetailsDto;
import com.mycompany.emailservice.exception.EmailServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EmailValidator {

    public void validateEmail(EmailDto emailDto) {
        emailDto.getRecipients().forEach(recipient -> {
                    validateDuplicates(emailDto.getCarbonCopyRecipients(), recipient);
                    validateDuplicates(emailDto.getBlindCarbonCopyRecipients(), recipient);
                }
        );

        // TODO further validations based on https://sendgrid.com/docs/API_Reference/Web_API_v3/Mail/errors.html#message.recipient-errors

    }

    private void validateDuplicates(List<String> recipientList, String recipient) {
        if (recipientList != null && recipientList.contains(recipient)) {
            throwEmailServiceException(recipient);
        }
    }

    private void throwEmailServiceException(String recipient) {
        Map<String, Object> errorVariable = new HashMap<>();
        errorVariable.put("First duplicated element:", recipient);
        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(HttpStatus.BAD_REQUEST,
                "Recipient cannot be present in multiple recipient list.", errorVariable);
        throw new EmailServiceException(errorDetailsDto);
    }
}
