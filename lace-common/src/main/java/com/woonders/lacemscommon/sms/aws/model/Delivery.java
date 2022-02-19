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
public class Delivery {
	String phoneCarrier;
	int    mnc;
	String destination;
	double priceInUSD;
	String smsType;
	int    mcc;
	String providerResponse;
	int    dwellTimeMs;
	int    dwellTimeMsUntilDeviceAck;
	
	
	
	@Override
	public String toString() {
		return "Delivery [phoneCarrier=" + phoneCarrier + ", mnc=" + mnc + ", destination=" + destination
				+ ", priceInUSD=" + priceInUSD + ", smsType=" + smsType + ", mcc=" + mcc + ", providerResponse="
				+ providerResponse + ", dwellTimeMs=" + dwellTimeMs + ", dwellTimeMsUntilDeviceAck="
				+ dwellTimeMsUntilDeviceAck + "]";
	}
}
