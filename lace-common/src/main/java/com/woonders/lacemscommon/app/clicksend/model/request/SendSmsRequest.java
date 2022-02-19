package com.woonders.lacemscommon.app.clicksend.model.request;

import java.util.List;

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
public class SendSmsRequest {

	@SerializedName("messages")
	List<SmsRequest> smsRequestList;
}
