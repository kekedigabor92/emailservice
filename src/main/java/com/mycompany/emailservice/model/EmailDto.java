package com.mycompany.emailservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@ApiModel(description = "All details about the email.")
public class EmailDto {

    private static final String VALID_EMAIL_REGEXP = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    private static final String THE_FROM_VALUE_IS_MANDATORY_CANNOT_BE_NULL_OR_BLANK = "The 'sender' value is mandatory and it should be a valid e-mail address.";
    private static final String THE_FROM_VALUE_MUST_BE_A_VALID_E_MAIL_ADDRESS = "The 'sender' value must be a valid e-mail address.";
    private static final String THE_RECIPIENTS_VALUE_IS_MANDATORY_THUS_IT_SHOULD_CONTAIN_AT_LEAST_ONE_VALID_E_MAIL_ADDRESS = "The recipients value is mandatory, thus it should contain at least one valid e-mail address.";
    private static final String THE_RECIPIENTS_SHOULD_CONTAIN_ONLY_VALID_E_MAIL_ADDRESSES = "The recipients should contain only valid e-mail addresses.";
    private static final String THE_CARBON_COPY_RECIPIENTS_SHOULD_CONTAIN_ONLY_VALID_E_MAIL_ADDRESSES = "The carbonCopyRecipients should contain only valid e-mail addresses.";
    private static final String THE_BLIND_CARBON_COPY_RECIPIENTS_SHOULD_CONTAIN_ONLY_VALID_E_MAIL_ADDRESSES = "The blindCarbonCopyRecipients should contain only valid e-mail addresses.";

    @NotBlank(message = THE_FROM_VALUE_IS_MANDATORY_CANNOT_BE_NULL_OR_BLANK)
    @Pattern(regexp = VALID_EMAIL_REGEXP, message = THE_FROM_VALUE_MUST_BE_A_VALID_E_MAIL_ADDRESS)
    @ApiModelProperty(required = true, name = "The sender email address")
    private String sender;

    @NotEmpty(message = THE_RECIPIENTS_VALUE_IS_MANDATORY_THUS_IT_SHOULD_CONTAIN_AT_LEAST_ONE_VALID_E_MAIL_ADDRESS)
    @ApiModelProperty(required = true, name = "The recipients email addresses")
    private List<@Pattern(regexp = VALID_EMAIL_REGEXP, message = THE_RECIPIENTS_SHOULD_CONTAIN_ONLY_VALID_E_MAIL_ADDRESSES) String>
            recipients;

    private List<@Pattern(regexp = VALID_EMAIL_REGEXP, message = THE_CARBON_COPY_RECIPIENTS_SHOULD_CONTAIN_ONLY_VALID_E_MAIL_ADDRESSES) String>
            carbonCopyRecipients;

    private List<@Pattern(regexp = VALID_EMAIL_REGEXP, message = THE_BLIND_CARBON_COPY_RECIPIENTS_SHOULD_CONTAIN_ONLY_VALID_E_MAIL_ADDRESSES) String>
            blindCarbonCopyRecipients;

}
