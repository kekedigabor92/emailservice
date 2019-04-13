package com.mycompany.emailservice.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@ApiModel(description = "All details about the email.")
public class EmailDto {

    private static final int EMAIL_ADDRESS_MAX_LENGTH = 320;

    static final String VALID_EMAIL_REGEXP = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    static final String SENDER_NULL_ERROR_MESSAGE = "The sender value is mandatory and it should be a valid e-mail address.";
    static final String SENDER_TOO_LONG_ERROR_MESSAGE = "The maximum length of the sender is " + EMAIL_ADDRESS_MAX_LENGTH;
    static final String SENDER_INVALID_ERROR_MESSAGE = "The sender must be a valid e-mail address.";
    static final String RECIPIENTS_CONTAINS_NULL_ERROR_MESSAGE = "The recipients should not contain null.";
    static final String RECIPIENTS_IS_MANDATORY_ERROR_MESSAGE = "The recipients is mandatory, thus it should contain at least one valid e-mail address.";
    static final String RECIPIENT_TOO_LONG_ERROR_MESSAGE = "The maximum length of a recipient is " + EMAIL_ADDRESS_MAX_LENGTH;
    static final String RECIPIENTS_CONTAINS_INVALID_EMAIL_ERROR_MESSAGE = "The recipients should contain only valid e-mail addresses.";
    static final String CARBON_COPY_RECIPIENTS_CONTAINS_NULL_ERROR_MESSAGE = "The carbonCopyRecipients should not contain null.";
    static final String CARBON_COPY_RECIPIENT_TOO_LONG_ERROR_MESSAGE = "The maximum length of a carbon copy recipient is " + EMAIL_ADDRESS_MAX_LENGTH;
    static final String CARBON_COPY_RECIPIENTS_CONTAINS_INVALID_EMAIL_ERROR_MESSAGE = "The carbonCopyRecipients should contain only valid e-mail addresses.";
    static final String BLIND_CARBON_COPY_RECIPIENTS_CONTAINS_NULL_ERROR_MESSAGE = "The blindCarbonCopyRecipients should not contain null.";
    static final String BLIND_CARBON_COPY_RECIPIENT_TOO_LONG_ERROR_MESSAGE = "The maximum length of a blind carbon copy recipient is " + EMAIL_ADDRESS_MAX_LENGTH;
    static final String BLIND_CARBON_COPY_RECIPIENTS_CONTAIN_INVALID_EMAIL_ERROR_MESSAGE = "The blindCarbonCopyRecipients should contain only valid e-mail addresses.";
    static final String SUBJECT_NOT_BLANK_ERROR = "The subject field should contain at least one character.";

    @NotNull(message = SENDER_NULL_ERROR_MESSAGE)
    @Length(max = EMAIL_ADDRESS_MAX_LENGTH, message = SENDER_TOO_LONG_ERROR_MESSAGE)
    @Pattern(regexp = VALID_EMAIL_REGEXP, message = SENDER_INVALID_ERROR_MESSAGE)
    @ApiModelProperty(required = true, name = "The sender email address")
    private String sender;

    @NotEmpty(message = RECIPIENTS_IS_MANDATORY_ERROR_MESSAGE)
    @ApiModelProperty(required = true, name = "The recipients email addresses")
    private List<
            @NotNull(message = RECIPIENTS_CONTAINS_NULL_ERROR_MESSAGE)
            @Length(max = EMAIL_ADDRESS_MAX_LENGTH, message = RECIPIENT_TOO_LONG_ERROR_MESSAGE)
            @Pattern(regexp = VALID_EMAIL_REGEXP, message = RECIPIENTS_CONTAINS_INVALID_EMAIL_ERROR_MESSAGE) String>
            recipients;

    private List<
            @NotNull(message = CARBON_COPY_RECIPIENTS_CONTAINS_NULL_ERROR_MESSAGE)
            @Length(max = EMAIL_ADDRESS_MAX_LENGTH, message = CARBON_COPY_RECIPIENT_TOO_LONG_ERROR_MESSAGE)
            @Pattern(regexp = VALID_EMAIL_REGEXP, message = CARBON_COPY_RECIPIENTS_CONTAINS_INVALID_EMAIL_ERROR_MESSAGE) String>
            carbonCopyRecipients;

    private List<
            @NotNull(message = BLIND_CARBON_COPY_RECIPIENTS_CONTAINS_NULL_ERROR_MESSAGE)
            @Length(max = EMAIL_ADDRESS_MAX_LENGTH, message = BLIND_CARBON_COPY_RECIPIENT_TOO_LONG_ERROR_MESSAGE)
            @Pattern(regexp = VALID_EMAIL_REGEXP, message = BLIND_CARBON_COPY_RECIPIENTS_CONTAIN_INVALID_EMAIL_ERROR_MESSAGE) String>
            blindCarbonCopyRecipients;

    @NotBlank(message = SUBJECT_NOT_BLANK_ERROR)
    private String subject;

    private String body;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public List<String> getCarbonCopyRecipients() {
        return carbonCopyRecipients;
    }

    public void setCarbonCopyRecipients(List<String> carbonCopyRecipients) {
        this.carbonCopyRecipients = carbonCopyRecipients;
    }

    public List<String> getBlindCarbonCopyRecipients() {
        return blindCarbonCopyRecipients;
    }

    public void setBlindCarbonCopyRecipients(List<String> blindCarbonCopyRecipients) {
        this.blindCarbonCopyRecipients = blindCarbonCopyRecipients;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
