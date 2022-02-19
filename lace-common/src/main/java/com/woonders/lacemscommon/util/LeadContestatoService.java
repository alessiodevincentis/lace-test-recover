package com.woonders.lacemscommon.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Formatter;
import java.util.List;

/**
 * Created by Giulio on 13/08/2019.
 */
@Slf4j
public class LeadContestatoService {

    public static String sendPOST(LeadContestato lead) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            String token = System.getProperty("COMPARATORE_TOKEN");
            String apiContestationUrl = System.getProperty("COMPARATORE_CONTESTATION_API_URL");
            // String apiContestationUrl = "http://93.46.10.54:55555/contesta-lead";
            // String apiContestationUrl = "http://test.devisprox.net:8050/api/api_contestation.php";  // DEVEL
            // String apiContestationUrl = "https://www.devisprox.com/api/api_contestation.php";  // PROD
            String salt = ""+lead.getDevisProxTenantId()+lead.getDevisProxLeadId();
            String encodedToken = "";
            String type = "HmacSHA256";

            byte[] key = token.getBytes("UTF-8");
            byte[] Sequence = salt.getBytes("UTF-8");
            Mac HMAC = Mac.getInstance(type);
            SecretKeySpec secretKey = new SecretKeySpec(key, type);
            HMAC.init(secretKey);
            byte[] Hash = HMAC.doFinal(Sequence);
            encodedToken = Base64.getEncoder().encodeToString(toHexString(Hash).getBytes());
            //encodedToken = Base64.getEncoder().encodeToString(Hash);

            HttpPost request = new HttpPost(apiContestationUrl);
            List<NameValuePair> params = new ArrayList<>(5);
            HttpEntity entity = MultipartEntityBuilder
                    .create()
                    .addTextBody("qid", lead.getDevisProxLeadId(), ContentType.TEXT_PLAIN)
                    .addTextBody("id_agents", lead.getDevisProxTenantId(), ContentType.TEXT_PLAIN)
                    .addTextBody("raison", ""+lead.getRequestReason(), ContentType.TEXT_PLAIN)
                    .addTextBody("commentaire", lead.getRequestNote(), ContentType.TEXT_PLAIN)
                    .addTextBody("token", encodedToken, ContentType.TEXT_PLAIN)
                    .build();
            request.setEntity(entity);
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            	String errorMsg = "{\"MSG\":\""+ response.getStatusLine().getReasonPhrase()+"\", \"STATUS\": 0}";
            	return errorMsg;
            }
            
            String responseString = new BasicResponseHandler().handleResponse(response);
            log.info("RESPONSE STRING " + responseString);

            return responseString;
        } catch (Exception ex) {
        	String errorMsg = "{\"MSG\":\""+ ex.getMessage()+"\", \"STATUS\": 0}";
        	return errorMsg;
            //return ("KO");
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
            	String errorMsg = "{\"MSG\":\" exception in httpClient.close() \", \"STATUS\": 0}";
            	return errorMsg;
            }
        }
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
