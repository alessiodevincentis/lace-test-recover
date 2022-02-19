package com.woonders.lacemscommon.sms.aws;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.AWSLogsClientBuilder;
import com.amazonaws.services.logs.model.DescribeLogStreamsRequest;
import com.amazonaws.services.logs.model.DescribeLogStreamsResult;
import com.amazonaws.services.logs.model.GetLogEventsRequest;
import com.amazonaws.services.logs.model.GetLogEventsResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.woonders.lacemscommon.sms.aws.model.SMSLog;



public class AWSCloudWatchManager {
	
	private AWSLogs logsClient;
	
	private static AWSCloudWatchManager sharedInstance;
	

	private AWSCloudWatchManager() {
		AWSStaticCredentialsProvider credentialProvider = AWSCredentialManager.shared().getCredentialProvider();
        
		logsClient = AWSLogsClientBuilder.standard()
							       		 .withRegion( Regions.US_EAST_1 )
							       		 .withCredentials(credentialProvider)
							       		 .build();
	}
	
	public static AWSCloudWatchManager shared(){
		if (sharedInstance == null){
			sharedInstance = new AWSCloudWatchManager();
		}
		
		return sharedInstance;
	}
	
	public List<SMSLog> getLogs() {
        DescribeLogStreamsRequest describeLogStreamsRequest = new DescribeLogStreamsRequest().withLogGroupName( "sns/us-east-1/689090582473/DirectPublishToPhoneNumber");
        DescribeLogStreamsResult  describeLogStreamsResult  = logsClient.describeLogStreams( describeLogStreamsRequest );


        List<SMSLog> lists       = new ArrayList<SMSLog>();
        Gson         gsonBuilder = new GsonBuilder().create();
        
        describeLogStreamsResult.getLogStreams().forEach( logStream -> {
            GetLogEventsRequest getLogEventsRequest = new GetLogEventsRequest()
                    .withLogGroupName( "sns/us-east-1/689090582473/DirectPublishToPhoneNumber" )
                    .withLogStreamName( logStream.getLogStreamName() );

            GetLogEventsResult result = logsClient.getLogEvents( getLogEventsRequest );

            List<SMSLog> smsLogs = result.getEvents().stream().map( outputLogEvent -> { 
            		return gsonBuilder.fromJson(outputLogEvent.getMessage(), SMSLog.class);
            }).collect(Collectors.toList());  
            
            lists.addAll(smsLogs);
        });
        
        return lists;
    }
	
	
	public static void main(String[] args) {
		Gson gsonBuilder = new GsonBuilder().create();
		List<SMSLog> logs = AWSCloudWatchManager.shared().getLogs();
		logs.forEach( log -> System.out.println(gsonBuilder.toJson(log)));
	}
}
