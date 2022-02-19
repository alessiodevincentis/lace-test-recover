package com.woonders.lacemscommon.app.clicksend.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.*;

/**
 * Created by Emanuele on 16/01/2017.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SmsRequest {

	private String source;
	private String to;
	
	// probably never used
	@SerializedName("list_id")
	private long listId;
	private String body;
	private String from;
	// Leave blank for immediate delivery. Your schedule time in unix format
	// http://help.clicksend.com/what-is-a-unix-timestamp
	private long schedule;
	
	@SerializedName("custom_string")
	private String customString;
	private String country;
	// An email address where the reply should be emailed to. If omitted, the
	// reply will be emailed back to the user who sent the outgoing SMS
	@SerializedName("from_email")
	private String fromEmail;

}
