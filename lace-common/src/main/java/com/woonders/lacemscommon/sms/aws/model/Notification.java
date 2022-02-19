package com.woonders.lacemscommon.sms.aws.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

	String messageId;
	String timestamp;
	
	
	@Override
	public String toString() {
		return "Notification [messageId=" + messageId + ", timestamp=" + timestamp + "]";
	}
}
