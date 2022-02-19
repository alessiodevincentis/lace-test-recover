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
public class SMSLog {
	Notification notification;
	Delivery delivery;
	String status;
	
	
	@Override
	public String toString() {
		return "SMSLog [notification=" + notification + ", delivery=" + delivery + ", status=" + status + "]";
	}
}
