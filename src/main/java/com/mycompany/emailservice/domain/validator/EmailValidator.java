package com.mycompany.emailservice.domain.validator;

import com.mycompany.emailservice.domain.model.EmailDto;
import com.mycompany.emailservice.domain.model.ErrorDetailsDto;
import com.mycompany.emailservice.exception.EmailServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EmailValidator {

    static final int TOTAL_RECIPIENTS_LIMIT = 1000;
    static final String TOTAL_RECIPIENTS_EXCEEDED = "The total recipients (recipients + carbonCopyRecipients + blindCarbonCopyRecipients) cannot be more than ";

    public void validateEmail(EmailDto emailDto) {
        validateTotalRecipientSize(emailDto);
        validateNoDuplicateIsPresentInAnyRecipientList(emailDto);
    }

    private void validateTotalRecipientSize(EmailDto emailDto) {
        int recipientSize = emailDto.getRecipients().size();
        int carbonCopyRecipientSize = emailDto.getCarbonCopyRecipients() != null ? emailDto.getCarbonCopyRecipients().size() : 0;
        int blindCarbonCopyRecipientSize = emailDto.getBlindCarbonCopyRecipients() != null ? emailDto.getBlindCarbonCopyRecipients().size() : 0;

        if (recipientSize + carbonCopyRecipientSize + blindCarbonCopyRecipientSize > TOTAL_RECIPIENTS_LIMIT) {
            ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(HttpStatus.BAD_REQUEST, TOTAL_RECIPIENTS_EXCEEDED + TOTAL_RECIPIENTS_LIMIT, null);
            throw new EmailServiceException(errorDetailsDto);
        }
    }

    private void validateNoDuplicateIsPresentInAnyRecipientList(EmailDto emailDto) {
        List<String> recipients = emailDto.getRecipients();
        List<String> carbonCopyRecipients = emailDto.getCarbonCopyRecipients();
        List<String> blindCarbonCopyRecipients = emailDto.getBlindCarbonCopyRecipients();

        recipients.forEach(recipient -> {
            validateRecipientIsPresentOnlyOnce(recipients, recipient);

            if (carbonCopyRecipients != null) {
                carbonCopyRecipients.forEach(ccr -> validateRecipientIsPresentOnlyOnce(carbonCopyRecipients, ccr));
                validateRecipientIsNotPresentInAnotherList(carbonCopyRecipients, recipient);
            }
            if (blindCarbonCopyRecipients != null) {
                blindCarbonCopyRecipients.forEach(bccr -> validateRecipientIsPresentOnlyOnce(blindCarbonCopyRecipients, bccr));
                validateRecipientIsNotPresentInAnotherList(blindCarbonCopyRecipients, recipient);
            }
            if (carbonCopyRecipients != null && blindCarbonCopyRecipients != null) {
                carbonCopyRecipients.forEach(ccr -> validateRecipientIsNotPresentInAnotherList(blindCarbonCopyRecipients, ccr));
            }
        });
    }

    private void validateRecipientIsPresentOnlyOnce(List<String> recipientList, String recipient) {
        int frequency = Collections.frequency(recipientList, recipient);
        if (frequency > 1) {
            throwEmailServiceException(recipient);
        }
    }

    private void validateRecipientIsNotPresentInAnotherList(List<String> recipientList, String recipient) {
        if (recipientList.contains(recipient)) {
            throwEmailServiceException(recipient);
        }
    }

    private void throwEmailServiceException(String recipient) {
        Map<String, Object> errorVariable = new HashMap<>();
        errorVariable.put("First duplicated element:", recipient);
        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(HttpStatus.BAD_REQUEST,
                "Each email address must be unique between the recipients, carbonCopyRecipients and blindCarbonCopyRecipients.", errorVariable);
        throw new EmailServiceException(errorDetailsDto);
    }
}
