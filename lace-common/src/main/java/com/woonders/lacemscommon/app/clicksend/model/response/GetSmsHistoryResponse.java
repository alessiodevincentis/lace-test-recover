package com.woonders.lacemscommon.app.clicksend.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.google.gson.annotations.SerializedName;

import lombok.*;

/**
 * Created by Emanuele on 18/01/2017.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetSmsHistoryResponse {

	private String direction;
	private LocalDateTime date;
	private String to;
	private String body;
	// TODO fare enum
	private String status;
	private String from;
	private LocalDateTime schedule;
	@SerializedName("status_code")
	// TODO fare enum
	private String statusCode;
	@SerializedName("status_text")
	private String statusText;
	@SerializedName("error_code")
	private String errorCode;
	@SerializedName("error_text")
	private String errorText;
	@SerializedName("message_id")
	private String messageId;
	@SerializedName("message_parts")
	private int messageParts;
	@SerializedName("message_price")
	private BigDecimal messagePrice;
	@SerializedName("from_email")
	private String fromEmail;
	@SerializedName("list_id")
	private String listId;
	@SerializedName("custom_string")
	private String customString;
	@SerializedName("contact_id")
	private long contactId;
	@SerializedName("user_id")
	private long userId;
	@SerializedName("subaccount_id")
	private long subaccountId;
	private String country;
	private String carrier;
	@SerializedName("first_name")
	private String firstName;
	@SerializedName("last_name")
	private String lastName;
	@SerializedName("_api_username")
	private String apiUsername;

}
