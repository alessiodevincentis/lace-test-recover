package com.woonders.lacemscommon.util;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.woonders.lacemscommon.exception.UnableToSendEmailException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.mail.simplemail.SimpleEmailServiceMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

/**
 * Created by ema98 on 3/4/2018.
 */
@Component
public class LaceMailSender {

    public static final String NAME = "laceMailSender";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private static final String FROM_EMAIL = System.getProperty("FROM_EMAIL", "info@nephis.it");
    //private static final String FROM_EMAIL = System.getProperty("FROM_EMAIL", "no-reply@lacems.com");
    
    private static final String CONFIGSET = "ConfigSet";

    @Autowired
    private MailSender mailSender;

    public void sendEmail(String recipientEmail, String agencyName, String replyTo, String subject, String body) throws UnableToSendEmailException {
        if (StringUtils.isEmpty(recipientEmail)) {
            throw new UnableToSendEmailException(UnableToSendEmailException.UnableToSendEmailError.MISSING_RECIPIENT);
        }
        if (StringUtils.isEmpty(agencyName)) {
            throw new UnableToSendEmailException(UnableToSendEmailException.UnableToSendEmailError.MISSING_AGENCY_NAME);
        }
        if (StringUtils.isEmpty(replyTo)) {
            throw new UnableToSendEmailException(UnableToSendEmailException.UnableToSendEmailError.MISSING_REPLY_TO);
        }
        if (StringUtils.isEmpty(subject)) {
            throw new UnableToSendEmailException(UnableToSendEmailException.UnableToSendEmailError.MISSING_SUBJECT);
        }
        if (StringUtils.isEmpty(body)) {
            throw new UnableToSendEmailException(UnableToSendEmailException.UnableToSendEmailError.MISSING_BODY);
        }
        
        
//       SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        try {
//            simpleMailMessage.setFrom(new InternetAddress(FROM_EMAIL, agencyName).toUnicodeString());
//        } catch (UnsupportedEncodingException e) {
//            simpleMailMessage.setFrom(FROM_EMAIL);
//        }
//        simpleMailMessage.setReplyTo(replyTo);
//        simpleMailMessage.setTo(recipientEmail);
//        simpleMailMessage.setSubject(subject);
//        simpleMailMessage.setText(body);
//        mailSender.send(simpleMailMessage);
//        
        
        try
        {
        	AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build();        
            
            SendEmailRequest request = new SendEmailRequest()
            		.withReplyToAddresses(replyTo)
            		.withDestination(
            				new Destination().withToAddresses(recipientEmail))
            		.withMessage(new Message()
            				.withBody(new Body() 
            						.withHtml(new Content()
            								.withCharset("UTF-8").withData(body))
            						.withText(new Content()
            								.withCharset("UTF-8").withData(body)))
            				.withSubject(new Content()
            						.withCharset("UTF-8").withData(subject)))
            		.withSource(FROM_EMAIL)
            		.withConfigurationSetName(CONFIGSET);
            
            
            client.sendEmail(request);
        }
        catch (Exception ex)
        {
        	throw new UnableToSendEmailException(ex.getMessage());
        }
        
        
    }

    @Bean
    public MailSender mailSender() {
        AmazonSimpleEmailService amazonSimpleEmailService = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build();
        return new SimpleEmailServiceMailSender(amazonSimpleEmailService);
    }

}