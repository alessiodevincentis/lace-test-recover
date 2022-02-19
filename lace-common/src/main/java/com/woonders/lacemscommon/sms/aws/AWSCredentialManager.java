package com.woonders.lacemscommon.sms.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

public class AWSCredentialManager {
	private static String accessKey       = System.getProperty("AWS_SMS_ACCESS_KEY");
	private static String secretAccessKey = System.getProperty("AWS_SMS_SECRET_KEY");
	
	private BasicAWSCredentials          basicAwsCredentials;
	private AWSStaticCredentialsProvider credentialsProvider;	
	
	private static AWSCredentialManager sharedInstance;
	
	
	private AWSCredentialManager() {
		basicAwsCredentials = new BasicAWSCredentials(accessKey,secretAccessKey);
		credentialsProvider = new AWSStaticCredentialsProvider(basicAwsCredentials);	
	}
	
	public static AWSCredentialManager shared() {
		if (sharedInstance == null)
			sharedInstance = new AWSCredentialManager();
		
		return sharedInstance;
	}

	public AWSStaticCredentialsProvider getCredentialProvider() {
		return credentialsProvider;
	}
	
}
