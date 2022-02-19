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
public class SendSmsResponse {

	private String direction;
	private LocalDateTime date;
	private String to;
	private String body;
	private String from;
	private LocalDateTime schedule;
	@SerializedName("message_id")
	private String messageId;
	@SerializedName("message_parts")
	private int messageParts;
	@SerializedName("message_price")
	private BigDecimal messagePrice;
	@SerializedName("custom_string")
	private String customString;
	@SerializedName("user_id")
	private long userId;
	@SerializedName("subaccount_id")
	private long subaccountId;
	private String country;
	private String carrier;
	// TODO fare enum
	private String status;
}
