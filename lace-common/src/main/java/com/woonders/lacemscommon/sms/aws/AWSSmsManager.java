package com.woonders.lacemscommon.sms.aws;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AWSSmsManager {


	private AmazonSNS snsClient;

	private static AWSSmsManager sharedInstance;
	

	private AWSSmsManager(){
		AWSStaticCredentialsProvider credentialProvider = AWSCredentialManager.shared().getCredentialProvider();
        
		snsClient = AmazonSNSClientBuilder.standard()
										  .withRegion(Regions.EU_WEST_1)
										  .withCredentials(credentialProvider)
										  .build();
	}

	public static AWSSmsManager shared(){
		if (sharedInstance == null){
			sharedInstance = new AWSSmsManager();
		}
		return sharedInstance;
	}

	public String sendSMSMessage(String senderID, String message, String destPhoneNumber) throws SmsNotSentException {
		try {
			//sender ID must contains at least one letters and the length must be 1 to 11 chars
//			if (senderID.contains("[a-zA-Z]+") == false && senderID.length() < 11) {
//				senderID = "A" + senderID;
//			}
			//prepend italian prefix +39 to destination number, if needed
			if (destPhoneNumber.startsWith("+") == false) {
				destPhoneNumber = "+39" + destPhoneNumber;
			}
			log.info("senderID " + senderID + " message: " + message + " destPhoneNumber " + destPhoneNumber);
			Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
			smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
					.withStringValue(senderID) //The sender ID shown on the device.
					.withDataType("String"));
			smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
					.withStringValue("0.50") //Sets the max price to 0.50 USD.
					.withDataType("Number"));
			smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
					.withStringValue("Promotional") //Sets the type to promotional.
					.withDataType("String"));
			PublishResult result = snsClient.publish(new PublishRequest()
					.withMessage(message)
					.withPhoneNumber(destPhoneNumber)
					.withMessageAttributes(smsAttributes));
			return result.getMessageId();
		}
		catch (Exception e) {
			throw new SmsNotSentException(e);
		}
	}

}