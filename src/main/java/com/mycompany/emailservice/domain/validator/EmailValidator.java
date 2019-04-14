package com.mycompany.emailservice.domain.validator;

import com.google.common.collect.ImmutableMap;
import com.mycompany.emailservice.domain.model.EmailDto;
import com.mycompany.emailservice.domain.model.ErrorDetailsDto;
import com.mycompany.emailservice.exception.EmailServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class EmailValidator {

    static final int TOTAL_RECIPIENTS_LIMIT = 1000;
    static final String TOTAL_RECIPIENTS_EXCEEDED = "The total recipients (recipients + carbonCopyRecipients + blindCarbonCopyRecipients) cannot be more than ";

    public void validateEmail(EmailDto emailDto) {
        validateTotalRecipientSize(emailDto);
        validateDuplicates(emailDto);
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

    private void validateDuplicates(EmailDto emailDto) {
        Optional<List<String>> recipients = Optional.ofNullable(emailDto.getRecipients());
        Optional<List<String>> carbonCopyRecipients = Optional.ofNullable(emailDto.getCarbonCopyRecipients());
        Optional<List<String>> blindCarbonCopyRecipients = Optional.ofNullable(emailDto.getBlindCarbonCopyRecipients());

        Set<String> uniques = new HashSet<>();
        Set<String> duplicates = Stream.of(recipients, carbonCopyRecipients, blindCarbonCopyRecipients)
                .flatMap(this::toStream)
                .filter(e -> !uniques.add(e))
                .collect(Collectors.toSet());

        if (!duplicates.isEmpty()) {
            throwEmailServiceException(duplicates);
        }
    }

    private Stream<String> toStream(Optional<List<String>> optional) {
        return optional.map(Collection::stream).orElseGet(Stream::empty);
    }

    private void throwEmailServiceException(Set<String> duplicatedRecipients) {
        Map<String, Object> errorVariable = ImmutableMap.of("Duplicates", duplicatedRecipients);
        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(HttpStatus.BAD_REQUEST,
                "Each email address must be unique between the recipients, carbonCopyRecipients and blindCarbonCopyRecipients.", errorVariable);
        throw new EmailServiceException(errorDetailsDto);
    }
}